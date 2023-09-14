# Rainy
A Weather application

Rainy is a sample Android project designed to showcase the use of various architectural layers and libraries for Android app development. It follows a modular architecture with the following layers:

Data
Domain
DI (Dependency Injection)
Presentation
Core
This project utilizes the following libraries and tools:

Dagger Hilt for Dependency Injection
Retrofit for Network Calls
JUnit and Mockito for Testing
Jetpack Compose for UI development
Project Structure
The project is organized into multiple modules, each responsible for a specific aspect of the application.

1. Data
The data module handles data-related operations. This includes data retrieval from remote sources, caching, and providing data to the domain layer. It uses Retrofit to make network calls and a local database for caching.

2. Domain
The domain module contains the core business logic of the application. It defines use cases, repositories, and data models that the rest of the application will use. The domain layer is independent of the data source and presentation, making it highly testable.

3. DI (Dependency Injection)
The di module utilizes Dagger Hilt to manage dependency injection across the application. It provides the necessary components and modules to inject dependencies into the various parts of the app, ensuring clean separation of concerns.

4. Presentation
The presentation module is responsible for the user interface and user interaction. It uses Jetpack Compose to create the UI components and implements the ViewModels that interact with the domain layer. The presentation layer is tightly connected to the UI and is responsible for rendering data and handling user input.

5. Core
The core module contains shared utilities and common code that are used across multiple modules. It may include extension functions, constants, or any code that is reusable throughout the application.

Getting Started
To run this project on your local development environment, follow these steps:

Clone the repository to your local machine.
shell
Copy code
git clone https://github.com/yourusername/rainy-android.git
Open the project in Android Studio.

Build and run the application on an emulator or physical device.

Testing
This project places a strong emphasis on testing. The test directory in each module contains unit tests for the respective module. We use JUnit and Mockito for unit testing, ensuring that each component of the application functions as expected.

To run the tests, you can use Android Studio's built-in testing tools or execute them via the command line.

shell
Copy code
./gradlew test
