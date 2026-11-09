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

## Extra rooms

The tutorial now includes a clockmaker's workshop and a rooftop observatory.
From the starting room, enter `go west`, `look bench`, `take lantern`, and
`go up`. The staircase is blocked until you have the lantern. Try
`look telescope` upstairs; return with `go down` and `go east`.
The original key/wardrobe puzzle and route to the hidden room still work.

## The Clockmaker's Winter

Narnia is now the start of a longer adventure rather than the final stop.
Follow the northern trail from the hidden room into a frozen valley and find
out why time has stopped. The telescope clue opens the winter gate; a keeper's
journal, a sealed chest, and a fountain lead to repairing the bell tower.
Six new locations continue the existing story, with a distinct sunrise ending.
All puzzles use the original YAML format; no engine changes are required.

Read room descriptions for directions and commands. Movement uses directions
(`go north`, `go up`), not room names. New interactions include `pull chain`,
`use gear`, `use crank`, and `ring bell`; their clues appear in the story.

<details>
<summary>Full walkthrough (spoilers)</summary>

From a new game:

```text
take key
go west
take lantern
go up
look telescope
go down
go east
go east
open wardrobe
take coat
go east
go north
go west
look journal
open chest
take gear
go east
go east
open gate
go north
go up
use gear
go down
pull chain
go up
use crank
ring bell
go east
```

</details>

## Command history

Enter `history` in either story to display a numbered list of earlier commands
in the current game session. It includes unsuccessful commands and preserves
the entered spelling, with leading/trailing spaces removed. Blank input,
`history` itself, and the exit command `quit` are not recorded. History starts
empty each time the application launches and does not replay commands or
change your room or puzzle states. This is an engine command; no YAML format
changes are needed. The startup text explains how to use it.
