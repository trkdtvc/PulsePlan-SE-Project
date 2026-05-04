# PulsePlan
PulsePlan is a mobile fitness and nutrition tracking application developed for the IT 309 Software Engineering project. The current version represents the initial release of the application and focuses on core user account and profile management functionality.

## Current Release Scope
This release includes:
- User registration
- User login
- User logout
- Viewing user profile
- Editing profile information
- Submitting updated profile data

The goal of this version is to provide a stable first release with the essential access and profile features fully implemented.

## Planned Next Release
The next release is planned to expand PulsePlan with additional fitness and nutrition tracking features, including:
- Home dashboard
- Calorie and macronutrient tracking
- Water intake tracking
- Weight goal progress
- Meal logging
- Food search and selection
- Workout timer
- Progress sharing
- General usability and performance improvements

## Technologies Used
- Kotlin
- Jetpack Compose
- Jetpack Navigation Compose
- Room Database
- Hilt
- DataStore
- Coil
- Gradle
- Android Studio

## Architecture Overview
PulsePlan is implemented as an Android mobile application with a local data layer. The application uses Room Database for persistence, DAO interfaces for database operations, repository classes for data access abstraction, Hilt for dependency injection, and DataStore for lightweight local storage.

## Database Entities
The local database is based on three main entities:
- User
- Food
- MealLog

The ER diagram for the database can be found in the `documentation` folder of this repository.

## Project Structure
Main folders in the project:
- `app/` – Android application source code
- `documentation/` – project documentation assets such as the ER diagram
- `gradle/` – Gradle wrapper and build configuration files

## How to Run the Project
1. Open the project in Android Studio.
2. Allow Gradle to sync completely.
3. Build the project.
4. Run the app on an emulator or physical Android device.

## Repository Information
This repository contains:
- The source code for the initial PulsePlan release
- Milestone documentation materials
- The ER diagram required for the Software Engineering project submission

## Authors
- Aid Ajkunić
- Tarik Dautović
