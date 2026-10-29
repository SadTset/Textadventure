# AI prompt history

The assignment asks for a **generated summary of prompts used to develop the solution**. This file records the prompts from the current Codex conversation in chronological order. Prompts are shortened or paraphrased where needed, while keeping their intent. Update this file as work continues; it is a summary, not a full chat transcript.

## 24 September 2026 — Understanding and setting up the project

| Prompt or request | Purpose and outcome |
| --- | --- |
| “Could you look into this project? There's something wrong with Gradle.” | Asked for error analysis. Codex inspected the Gradle setup, reproduced a network restriction during wrapper download, then ran a successful build and found a Java toolchain deprecation. It added the Foojay toolchain resolver to `settings.gradle.kts` and verified a clean build without that warning. |
| “For the future ask me first before you commit something.” | Set the project workflow rule: no commits without asking first. No commit was made. |
| “So you fixed the Gradle problem?” | Asked for confirmation of the verified Gradle change and its uncommitted status. |
| “Can you look through this project and tell me what the task is?” | Asked Codex to read the assignment and summarize the product, competency check, and submission requirements. |
| “Can you look into that one HTML file? In there, there's the whole assignment.” | Directed Codex to check the original HTML assignment rather than rely only on its Markdown copy. The HTML confirmed the YAML example and requirements. |
| “Is the YAML file already made? If not, could you make it?” | Asked Codex to create `src/main/resources/tutorial.yaml` from the supplied assignment example. |
| “Could you make a plan.md file, where there's a step by step manual on how to do that assignment?” | Requested a guide for completing the project. The first version was a checklist. |
| “For some reason, the Gradle files are still showing red in my IDE.” | Asked for diagnosis. Codex found the main project's Gradle files were untracked by Git and explained that IDE filename colors may reflect Git status. |
| “Where did you put in the plan.md?” and “I need everything in the same repo?” | Corrected a workspace mix-up: the plan had first been made in a separate Git worktree. Codex copied it into the main project folder, alongside Gradle and YAML. |
| “With the plan, I meant write a step by step tutorial/manual on how to execute this assignment.” | Clarified the desired guide. Codex rewrote `plan.md` with implementation instructions, commands, and checks for each step. |
| “Why are still so many files red in this project?” | Asked again about IDE colors. Codex identified the untracked files in the main project's Git status; no commit was made. |
| “Read the assignment again about that history.md file and make it.” | Requested this generated prompt summary, as required under *Abgabe* in the HTML assignment. |
| “So the files that are red, especially the Gradle ones, aren't red anymore when they're committed?” | Asked whether IDE filename colors reflect Git status. Codex explained the difference between untracked filenames and errors inside a file. |
| “Aight, commit it then.” | Authorized a commit. Codex ran the Gradle build and committed the setup, tutorial YAML, plan, history, and README as `8107b8f` (`Add Gradle setup and assignment guides`). |
| “Look at the assignment.md file again and correct it if it's wrong; it has to be basically 1:1 from the HTML file.” | Asked for a source comparison. Codex verified that the 107 YAML lines matched exactly and added the HTML version and date to the Markdown header. |
| “What's the next step?” | Asked where to begin implementation. Codex pointed to the first manual step: make Gradle run a Java entry point. |
| “Are the steps in line with the assignment?” | Asked for an audit of the guide. Codex clarified which items are required and which are suggested implementation choices, and updated `plan.md` accordingly. |
| “You remember to automatically update the history.md, right?” | Requested ongoing prompt tracking. Codex updated this summary with the prompts since its first version. |
| “Can we start with the assignment now?” | Started Step 1 of the manual. Codex added the Gradle `application` plugin and a Java `Main` entry point, then verified `clean build` and `run`. The entry point currently prints a startup message; YAML loading comes next. |
| “Doesn't the file have to look like the YAML file? Should we copy the whole block?” | Clarified that `tutorial.yaml` already contains the full story example; Java model types will represent its structure and Jackson will read it. `Main.java` is only the launcher. |
| “Shouldn't I first have a story for this game, or should we finish the code and then change it?” | Discussed implementation order. The supplied tutorial can drive engine development and testing; original story rooms can be planned now and added later without changing the YAML format. |
| “I don't [have a story]; continue with the next step.” | Started Step 2: Codex defined Java records for the YAML data, added a Jackson YAML loader with room-reference validation, and added tests for the supplied tutorial, a different story, and an invalid target. The application now prints the starting room name loaded from YAML. |
| “Aight do step 3.” | Started the MVC structure. Codex added mutable game state, a console view, and a controller that prepares introduction and room text without reading the console. `Main` wires them together; a controller test and Gradle run verified the output. Command input remains for Step 4. |
| “Should I commit after every step or only when I stop working for the day?” | Asked about commit frequency. Codex clarified that the assignment requires at least one commit per working week; a commit at the end of a work session is fine when the changes form a coherent, verified piece of progress. |
| “Do the next step.” | Started Step 4. Codex added the console command loop, `quit` and end-of-input handling, and movement through unconditional YAML room actions. It verified `go east`, `go west`, and `quit` in a scripted Gradle run. Conditional actions and full YAML responses remain for later steps. |
| “Could you check off the steps we have done from now on?” | Requested progress tracking in the manual. Codex marked Steps 1–4 complete in `plan.md` and will update each remaining step after its check passes. |
| “You can commit now; make sure to put in all the things we've done.” / “Write all the steps we've done in the commit message.” | Authorized a commit of the completed work and requested a detailed message listing Steps 1–4 and the related documentation changes. |
| “Continue with the next step.” | Completed Step 5. Codex implemented first-matching conditional YAML actions, state additions, messages, and room changes. Tests and a scripted console playthrough reached the tutorial's secret room. The plan now marks Step 5 complete. |
| “Go ahead.” | Continued with Step 6. Codex added global verb synonyms, YAML-defined `help` responses, and the correct global error messages for unknown verbs or objects. Tests and a scripted console run verified the responses; the plan now marks Step 6 complete. |
| “Commit it.” | Authorized a focused commit of the completed Step 5 and Step 6 gameplay rules, tests, and documentation updates. |

## 1 October 2026 — Reviewing the project and adding story selection

| Prompt or request | Purpose and outcome |
| --- | --- |
| “Who deserializes the YAMLs to Java records?” / “Who’s Jackson?” | Asked how YAML loading works. Codex identified Jackson's `YAMLMapper` in `StoryLoader` and explained that Jackson is the library that creates the Java records from YAML data. |
| “Could you look into this project and see where we left off?” | Requested a project status review. Codex checked Git, the implementation, plan, history, and tests. Steps 1–6 were complete, tests passed, and Step 7 was identified as the next task. |
| “Continue with the next step.” | Completed Step 7. Codex added a second forest story, optional story-file selection in `Main`, clear errors for missing or malformed files, and tests for default and external loading. Automated tests and console runs verified both stories, and the README and plan were updated. |

## 29 October 2026 — Context review and commit authorization

| Prompt or request | Purpose and outcome |
| --- | --- |
| “look through this folder for context and instructions” | Hermes reviewed the project instructions, assignment, plan, history, implementation, stories, and tests. A forced fresh test run passed. Step 7 was identified as complete but uncommitted, with Step 8 next. |
| “aight do the commit” | Authorized a focused commit of Step 7: external story selection, the forest example, loading tests, and documentation. The Gradle build passed before committing. |

## Continue this log

After each meaningful AI assisted work session, add the date, a short summary of the prompt(s), and what changed or was learned. Include learning, debugging, review, and implementation prompts. Keep the summary accurate; do not invent prompts or claim work that has not been completed.
