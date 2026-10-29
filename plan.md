# How to complete the text adventure assignment

Follow this manual one step at a time. Run the check at the end of each step before moving on. The [HTML assignment](TextadventureAssignment_260923-a6547c5c.html) is the authoritative specification. Tests, command line story selection, and `quit` are suggested ways to implement and verify the required behavior; the assignment does not mandate those exact mechanisms.

Progress: Steps 1–7 are complete. Update each checkbox only after its check passes.

Open **`C:\Users\detek\nextcloud\school\BBW\320\Textadventure`** in your IDE. That main project folder already contains the Gradle files and `src/main/resources/tutorial.yaml`. Run the commands below in its terminal. On Windows PowerShell, Gradle is `.\gradlew.bat`; on macOS/Linux, use `./gradlew`.

## [x] 1. Make Gradle launch Java

Run `.\gradlew.bat clean build --warning-mode all`. At present, `compileJava NO-SOURCE` is expected. Add the `application` plugin in `build.gradle.kts` and set its `mainClass`, for example to `ch.bbw.textadventure.Main`. Create that class under `src/main/java/ch/bbw/textadventure/` with a `main(String[] args)` method that prints a startup message. Keep the existing Java 25 toolchain, resolver, and Jackson YAML dependency. Run `.\gradlew.bat run`.

**Check:** `build` and `run` succeed and the startup message appears. A red filename in the IDE may simply mean Git has not tracked it; inspect any actual error text separately.

## [x] 2. Read the story format and model it

Read `tutorial.yaml` from top to bottom. It is data for the engine: `rooms` holds rooms by ID; each room's `verbs` maps a verb and object to an **ordered list** of actions. The Java engine should never hardcode the tutorial's room IDs or puzzle.

Create model types with these fields:

| YAML part | Suggested Java representation |
| --- | --- |
| Whole file | `Story`: `startRoom`, `description`, `rooms`, `globals` |
| `rooms.<id>` | `Room`: `name`, `description`, `verbs` |
| `verbs` | `Map<String, Map<String, List<Action>>>` |
| Action list item | `Action`: optional `ifState`, `addState`, `message`, `room` |
| `globals.<verb>` | `GlobalVerb`: `synonyms`, `unknownVerb`, `unknownObject` |

Use Jackson's YAML mapper to deserialize `/tutorial.yaml` from the classpath into `Story`. Records or ordinary classes can work; choose a form Jackson can construct and be able to explain it. Validate that `startRoom` exists and every action's `room` target exists. Give a useful error for invalid data. Add a test that loads the tutorial and checks nested actions, plus a second tiny fixture with different room IDs.

**Check:** `.\gradlew.bat test` passes and the application can print the start room's name from YAML.

## [x] 3. Set up MVC responsibilities

Keep the **model** (story and mutable game state), **view** (console input/output), and **controller/engine** (command parsing and action rules) separate. For example, `GameState` can hold the current room ID and a `Set<String>` of states, `ConsoleView` can read/print, and `GameController` can interpret commands. Keep `Main` short: wire these pieces together and start the game.

**Check:** You can test a command by calling the controller without typing into a console, and you can point to the class that owns each responsibility.

## [x] 4. Add the console loop and movement

Print the story introduction and starting room. Repeatedly read a console line, split it into a verb and optional object (`go east` → `go`, `east`), and pass it to the controller. Trim extra spaces and ignore blank input. Provide a `quit` command and stop at end of input so scripted runs can finish.

First support actions containing `room`: set the current room ID and print the destination name and description. Do not assume every action has a `room` field. The `quit` command is a practical way to end the loop; it is an implementation choice, not a separate assignment requirement.

**Check:** `go east` moves to `room-two`, and `go west` moves back to `room-one`.

## [x] 5. Add conditional actions and state

For the chosen verb/object, examine actions **in YAML order**. An action without `ifState` matches automatically. One with `ifState` matches only if the state set contains that value. Execute **only the first matching action**. If none matches, do not change game state.

For the chosen action, add `addState` when present, print `message` when present, and change room when `room` is present. One action can do several things. Keep the state set when moving between rooms.

**Check:** `take key`, `go east`, `open wardrobe`, `take coat`, `go east` reaches `secret`. A second `take key` reports that you already have it. Test both matching and nonmatching conditions.

## [x] 6. Add synonyms, help, and error messages

Resolve global synonyms before searching room verbs: `walk east` should work like `go east`, and `examine table` like `look table`. For a known verb absent from the room, use its global `unknownVerb`. For a verb present without the requested object, use `unknownObject`, replacing `{}` with the entered object. For an unknown verb, use `globals.default.unknownVerb`. Support `help` and `help verbs` using the help messages in YAML. Do not print `null` or an empty error line.

**Check:** Try `walk east`, `examine table`, `go north`, `open key`, `dance`, `help`, and `help verbs`. Compare output to YAML and add tests for the distinct error cases.

## [x] 7. Prove the engine works with another story

Create a second small story with different room names and actions but the same YAML structure, then run it through the same application. One practical way to select a story is an optional YAML path argument, with the bundled tutorial as the default. This interface is an implementation choice; the requirement is that the engine can play any story in the specified format. Report malformed YAML or a missing file clearly.

**Check:** The second story runs without editing Java source. The original tutorial still works.

## [ ] 8. Add your own rooms and a visible feature

Extend `tutorial.yaml` with original rooms, linked from an existing room. A new puzzle would make those rooms more interesting, but the assignment only explicitly requires your own rooms. Preserve the original route to `secret`. Do not change the YAML format without explicit agreement from the teacher.

Choose one feature players can use. For example, a `history` command can show earlier commands, or `save`/`load` can persist the current room and states. Put its logic in the appropriate model/controller/view classes, explain it in the game and README, and test it. An application command can be added without changing the story YAML format.

**Check:** A player can find the new rooms and use the feature; the original tutorial puzzle still passes.

## [ ] 9. Review code and prepare for the written check

Run `.\gradlew.bat clean build --warning-mode all`, play the game, and run SonarLint in the IDE. Fix warnings, especially long methods. Keep code in English, use `var` wherever possible, and deliberately use and understand Java 25's newer language features. Prefer Jackson as the only runtime library and check that dependencies are current.

Practise explaining records versus beans (constructors, accessors, immutability, value semantics); `try`/`catch`/`finally` and swallowed exceptions; basic YAML syntax; how YAML maps to Java objects; your MVC split; and a complete path from a YAML action to console output. Practise specific AI prompts for learning, examples, review, debugging, ideas, and reflection.

**Check:** You can demonstrate and explain your own code without reading a generated answer.

## [ ] 10. Finish the submission

Update `README.md` with setup, run commands, command examples, story selection, and your extra feature. Keep the existing `history.md` updated as a generated summary of prompts used. Maintain `AGENTS.md` with your personal agent rules. Write `Selbstevaluation.md` **yourself**, at least 200 words, covering your opinion of the project and agentic programming, your extra feature and proudest work, and how you gained the competencies. Use AI through the IDE as required by the assignment, and keep hand edits to code minimal.

Run the build and a playthrough from a fresh checkout. Submit the complete project in a **private GitHub repository**. The assignment requires at least one commit per working week. After each small completed piece, review the changes and make a focused commit; **ask before creating a commit**.
