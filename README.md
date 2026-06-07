# PulsePlan
PulsePlan is a mobile fitness and nutrition tracking application developed for the IT 309 Software Engineering project. The application helps users manage their nutrition profile, track daily meals, calculate calories and macronutrients, track water intake, use a running timer, and share progress.

## Milestone 3 Release Scope
This release focuses on improving persistence, profile editing, meal tracking, water tracking, testing, and documentation. The application now preserves user data properly and includes more complete daily tracking functionality.

Current release features include:

- User registration
- User login
- User logout
- Viewing user profile
- Editing profile information
- Saving updated age, height, weight, and activity level
- Home dashboard with daily calorie and macronutrient overview
- Meal logging
- Meal deletion from the Home screen
- Food database seeding without wiping existing app data
- Water intake tracking
- Editable daily water goal
- Running timer with burned-calorie tracking
- Progress sharing screen
- Local persistence using Room Database and DataStore
- Unit tests for important Milestone 3 logic

## Technologies Used

- Kotlin
- Jetpack Compose
- Jetpack Navigation Compose
- Room Database
- Hilt
- DataStore
- Kotlin Coroutines
- Kotlin Flow and StateFlow
- Coil
- Gradle
- Android Studio

## Architecture Overview
PulsePlan uses the MVVM architectural pattern. Jetpack Compose screens act as the View layer, ViewModel classes manage UI state and user actions, and the data layer is handled through Room entities, DAO classes, repository classes, and DataStore managers.

The project also uses the Repository Pattern to keep database access separate from UI logic, Singleton-style shared resources through Hilt, Dependency Injection through Hilt modules and constructor injection, and Observer-style reactive updates through Kotlin Flow and StateFlow.

More details are available in:

```text
documentation/Architecture_and_Design_Patterns.md
```

## Database Entities

The local Room database is based on three main entities:

- User
- Food
- MealLog

The ER diagram for the database can be found in the `documentation` folder of this repository.

## Project Structure

Main folders in the project:

- `app/` – Android application source code
- `app/src/main/java/com/example/dietplanner/dao/` – Room DAO interfaces
- `app/src/main/java/com/example/dietplanner/database/` – Room database definition
- `app/src/main/java/com/example/dietplanner/model/` – entity/model classes
- `app/src/main/java/com/example/dietplanner/repository/` – repository interfaces and implementations
- `app/src/main/java/com/example/dietplanner/session/` – session and water intake DataStore managers
- `app/src/main/java/com/example/dietplanner/ui/theme/screens/` – Jetpack Compose screens
- `app/src/main/java/com/example/dietplanner/ui/theme/screens/viewmodel/` – ViewModel classes
- `app/src/test/java/com/example/dietplanner/` – unit tests
- `documentation/` – project documentation assets and architecture/design pattern documentation
- `gradle/` – Gradle wrapper and build configuration files

## Tests

Milestone 3 includes unit tests for important application logic and repository behavior. The tests cover daily calorie calculation, water intake add/remove/reset behavior, water goal validation, profile update persistence, and meal deletion through the repository layer.

Run the tests with:

```powershell
.\gradlew testDebugUnitTest
```

## How to Run the Project

1. Open the project in Android Studio.
2. Allow Gradle to sync completely.
3. Build the project.
4. Run the app on an emulator or physical Android device.

## Repository Information

This repository contains the source code, documentation materials, ER diagram, Milestone 3 implementation updates, and unit tests for the PulsePlan Software Engineering project.

## Authors

- Aid Ajkunić
- Tarik Dautović
