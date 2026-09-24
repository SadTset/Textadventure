# Textadventure

Java 25 and Gradle project for the YAML-driven text adventure assignment.

The supplied story is in `src/main/resources/tutorial.yaml`. The application
loads it with Jackson and displays the introduction and starting room through
the console view. You can currently enter `go east`, `go west`, and `quit`.
Other actions, conditions, synonyms, and YAML error messages are still being
implemented.

Run `./gradlew build` (PowerShell: `.\gradlew.bat build`) to check the project.
Run `./gradlew run` (PowerShell: `.\gradlew.bat run`) to launch the current
application entry point. Run `./gradlew test` (PowerShell: `.\gradlew.bat test`)
to check story deserialization and validation.
The Gradle Java toolchain is set to Java 25.
