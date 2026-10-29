# Textadventure

Java 25 and Gradle project for the YAML-driven text adventure assignment.

The supplied story is in `src/main/resources/tutorial.yaml`. The application
loads it with Jackson and displays the introduction and starting room through
the console view. The tutorial's key, wardrobe, coat, and secret-room puzzle
works through YAML actions. Verb synonyms, `help`/`help verbs`, and error
messages are also read from the YAML. Enter `quit` to leave the game.

Run `./gradlew build` (PowerShell: `.\gradlew.bat build`) to check the project.
Run `./gradlew run` (PowerShell: `.\gradlew.bat run`) to launch the current
application entry point. Run `./gradlew test` (PowerShell: `.\gradlew.bat test`)
to check story deserialization and validation.
The Gradle Java toolchain is set to Java 25.

The bundled tutorial is used when no story path is supplied. To run another
story with the same YAML structure, pass its path as the single application
argument. For example, the project includes a second playable forest story:

```powershell
.\gradlew.bat run --args="src/main/resources/forest.yaml"
```

A missing file, malformed YAML, or invalid story structure is reported with
the path of the story that could not be loaded.
