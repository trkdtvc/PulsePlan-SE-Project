# Architecture and Design Patterns

## Architecture Pattern: MVVM
PulsePlan uses the MVVM architectural pattern, which separates the user interface, application logic, and data access logic into clear layers. This makes the application easier to maintain, test, and extend because each part of the app has a specific responsibility.

In PulsePlan, the View layer is built with Jetpack Compose screens such as `LoginScreen`, `RegisterScreen`, `HomeScreen`, `AddMealScreen`, `UserProfileScreen`, `RunningTimerScreen`, and `ShareScreenProgress`. These screens are responsible for displaying the interface and reacting to user actions. They do not directly handle database operations.

The ViewModel layer contains classes such as `UserViewModel`, `MealLogViewModel`, `MealViewModel`, `FoodViewModel`, `WaterIntakeViewModel`, and `ShareProgressViewModel`. These classes hold screen-related state and call the repository or manager classes when data needs to be loaded, updated, deleted, or saved. For example, `UserViewModel` handles login, registration, logout, loading the current user, and saving profile edits. `MealLogViewModel` handles meal logging and deletion. `WaterIntakeViewModel` controls the water intake actions used by the Home screen.

The Model/Data layer contains the Room entities, DAO interfaces, repositories, and local storage managers. The main model classes are `User`, `Food`, and `MealLog`. The DAO classes such as `UserDao`, `FoodDao`, and `MealLogDao` define the database operations. The repository classes then wrap those DAO operations and provide a cleaner interface for the ViewModels.

This structure keeps the UI separate from database code. For example, the Home screen does not directly delete a meal from Room. Instead, it calls the ViewModel, the ViewModel calls the repository, the repository calls the DAO, and Room updates the database.

## Design Pattern 1: Repository Pattern
PulsePlan uses the Repository Pattern to separate database access from the rest of the application. Instead of making the ViewModels communicate directly with Room DAO classes, the ViewModels communicate with repository interfaces.

The main repository files are:

- `UserRepository.kt`
- `UserRepositoryImpl.kt`
- `FoodRepository.kt`
- `FoodRepositoryImpl.kt`
- `MealLogRepository.kt`
- `MealLogRepositoryImpl.kt`

This pattern improves the project because it hides the exact database implementation from the ViewModels. If the app later changes from only local Room storage to a remote API, the ViewModels would not need major changes because the repository layer would handle that change.

A clear example is meal deletion. The Home screen asks `MealLogViewModel` to delete a meal. The ViewModel then calls `MealLogRepository`, and the repository calls `MealLogDao`. This keeps the database logic organized and prevents the UI from becoming responsible for data storage details.

## Design Pattern 2: Singleton Pattern
PulsePlan uses Singleton-style objects for shared application-level resources that should only have one instance while the app is running. This is mainly handled through Hilt dependency injection with the `@Singleton` annotation.

The main examples are found in `DatabaseModule.kt`:

- `AppDatabase` is provided as a singleton so the app uses one Room database instance.
- `UserDao`, `FoodDao`, and `MealLogDao` are provided from that same database instance.
- `UserRepository`, `FoodRepository`, and `MealLogRepository` are provided as singleton repository instances.
- The Preferences DataStore instance is also provided as a singleton.

This prevents the app from creating multiple unnecessary database or DataStore objects. It also makes the code safer because all parts of the app use the same shared source of stored data.

## Design Pattern 3: Dependency Injection
PulsePlan uses Dependency Injection through Hilt. Instead of manually creating database, DAO, repository, and manager objects inside each screen or ViewModel, Hilt provides those dependencies automatically.

The main dependency injection setup is in `DatabaseModule.kt`. This module tells Hilt how to create and provide the Room database, DAO classes, repository implementations, and DataStore instance.

ViewModels receive the objects they need through constructor injection. For example, `UserViewModel` receives `UserRepository` and `SessionManager`, while `WaterIntakeViewModel` receives `WaterIntakeManager`. This makes the code cleaner because each class only declares what it needs, and Hilt handles object creation.

Dependency Injection also improves testability. Because ViewModels and repositories depend on interfaces or injected objects, tests can use fake or in-memory versions of those dependencies instead of relying on the full real app environment.

## Observer Pattern / Reactive State
PulsePlan also uses an Observer-style approach through Kotlin Flow and StateFlow. This is visible in classes such as `SessionManager`, `WaterIntakeManager`, `UserViewModel`, and `WaterIntakeViewModel`.

For example, `WaterIntakeManager` exposes `waterStateFlow`, and `WaterIntakeViewModel` converts it into a `StateFlow` called `waterState`. The Home screen observes this state, so when the user adds, removes, resets, or changes the water goal, the UI updates automatically.

The same idea is used for login/session state. `SessionManager` exposes `userIdFlow`, and the app can react when the logged-in user changes or logs out.

## Room Database and Local Persistence
PulsePlan uses Room as the local database system. The database is defined in `AppDatabase.kt`, and it contains three main entities:

- `User`
- `Food`
- `MealLog`

Room is used for structured app data such as users, foods, and meal logs. DataStore is used for lighter key-value style data such as the logged-in user id and water intake preferences.

During Milestone 3, startup database behavior was improved so the application no longer wipes and reinserts data every time it launches. Default foods are now safely seeded only when the food table is empty. This allows user data, profile edits, saved meals, and progress to persist correctly.

## How the Layers Work Together
A typical PulsePlan flow works like this:

1. The user interacts with a Jetpack Compose screen.
2. The screen calls a ViewModel function.
3. The ViewModel updates state or calls a repository/manager.
4. The repository/manager performs the database or DataStore operation.
5. Room or DataStore stores the change.
6. Flow/StateFlow updates the ViewModel state.
7. The Compose UI recomposes and shows the updated data.

This layered structure supports the main Milestone 3 features: profile editing, persistent database seeding, meal deletion, water intake tracking, and unit-tested business logic.
