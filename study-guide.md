# Written-check practice

Use this guide for practice, not as your personal Selbstevaluation.md. First answer each question without looking at the source, then check the explanation and demonstrate it in the project.

## 1. Records versus beans

Find Story, Room, Action, and GlobalVerb in src/main/java/ch/bbw/textadventure/model.

A record declares components and supplies a canonical constructor, component accessors such as room(), and value-based equals, hashCode, and toString. A typical bean uses explicit fields, constructors, getters, and often setters. Records are final and their component references cannot be reassigned, but that is shallow immutability: a referenced Map can still be mutable. GameState is deliberately a mutable class; states() and commandHistory() return immutable copies.

Practice: Explain why Action can be a record but the current room changes in GameState. Explain why var does not mean dynamic typing; the compiler infers a fixed local-variable type. var cannot replace record component, field, or method parameter types.

## 2. YAML to Java

StoryLoader.load calls YAMLMapper.readValue(input, Story.class). Jackson is the library that constructs the nested records, maps, and action lists. Java does not execute YAML directly.

Trace this shape: rooms -> room ID -> Room -> verbs -> verb -> object -> ordered List<Action>. Indentation creates nesting, a dash creates a list entry, square brackets describe an inline list, and | introduces a multiline string. Quote values containing a colon followed by a space. Room IDs are data, not Java class names.

Practice: On paper, describe a room called cellar with a go/up action pointing to workshop and a look/chest message. Then write valid YAML and check indentation against tutorial.yaml. Explain the difference between an absent ifState and an ifState whose state has not been acquired.

## 3. Exceptions and resources

In Main.loadStory, try-with-resources closes each opened stream on success or failure. For an external story, IOException or IllegalArgumentException is caught and wrapped in IOException with the path and original cause. The exception is not silently swallowed. main currently propagates IOException, so a loading failure can appear as a stack trace, not a friendly error screen.

Practice: Explain what runs when opening the file fails versus when parsing fails. Explain that a finally block normally runs when leaving try/catch, including via return or throw, but is not an absolute guarantee if the JVM terminates. Explain why catching an exception and ignoring it hides failure.

## 4. MVC responsibilities

Model: story records describe loaded data; GameState owns the current room, acquired states, and command history. StoryLoader converts and validates data.

Controller: GameController parses commands, resolves synonyms, chooses the first matching action, updates the model, and returns text.

View: ConsoleView reads UTF-8 console input and prints text and prompts. Main wires these pieces together and owns the application loop and quit/EOF handling.

Practice: Point to each responsibility. Explain why the controller can be tested without typing into a console. Text formatting currently lives partly in the controller, while the view handles actual I/O.

## 5. Trace a complete action

Use take key -> go east -> open wardrobe -> take coat -> go east.

For open wardrobe, follow the input through Main.runGame, GameController.handleCommand, canonicalVerb, handleStoryCommand, firstMatchingAction, and execute. The first action requires key. If present, execute adds wardrobeOpen and returns the message. Otherwise the unconditional fallback matches. Only the first matching action executes. ConsoleView.show prints the returned text. A later room action updates currentRoomId and prints the destination text.

Practice: Explain why an unconditional action placed before a conditional action would hide it. Explain why states remain available after moving rooms.

## 6. Java 25

Main uses import module java.base, finalized in Java 25 by JEP 511. It makes public types from the module's exported packages available on demand, including IOException, Files, and Path. It is not a dependency download, does not import submodules, and does not require module-info.java. Explicit class imports are still useful for resolving ambiguous names. No preview flag is needed.

Records and var are modern Java features, but neither was introduced in Java 25. Do not present them as Java-25-specific features.

Gradle selects a Java 25 toolchain even if java -version in your terminal reports Java 17. Use .\gradlew.bat -q javaToolchains to inspect installations. Configure the IDE project SDK/language level to 25 as well.

Reference: https://openjdk.org/jeps/511

## 7. Extra feature and demonstration

history displays numbered earlier commands in the current session, including failures, without replaying them. Blank input, history itself, and quit are excluded. The model stores the list; the controller formats it; the view prints it. It works independently of story YAML. It is not a save system.

Demonstrate go west -> go up (blocked) -> take lantern -> go up -> look telescope -> history. Return with go down -> go east and complete the original tutorial puzzle.

## 8. Specific AI prompts

Learning: Explain the shallow immutability of our Story record and its rooms Map.
Example: Give me a different YAML action list with one conditional action and one fallback.
Review: Check StoryLoader validation for data that causes a later NullPointerException.
Debugging: Explain this actual stack trace and trace the failing input; do not change code yet.
Ideas: Suggest two extra puzzles using only ifState, addState, message, and room.
Reflection: Critique our MVC boundaries and explain one trade-off without rewriting the project.

## Checks you must perform yourself

- Run SonarQube for IDE (formerly SonarLint) on src/main/java and src/test/java. Install/enable it if missing, reanalyze after fixes, and inspect all findings. A passing Gradle build is not a Sonar result.
- Explain the topics above without reading this guide. Use a short mock written exam or ask for a quiz.
- Write Selbstevaluation.md personally (at least 200 words); do not submit this generated guide as your reflection.
