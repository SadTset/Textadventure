# AI prompt history

The assignment asks for a **generated summary of prompts used to develop the solution**. This file records the prompts from development conversations in chronological order. Prompts are shortened or paraphrased where needed, while keeping their intent. Update this file as work continues; it is a summary, not a full chat transcript.

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
| “what's the next step” / “continue then” | Started and completed Step 8. Hermes added a clockmaker's workshop and lantern-gated rooftop observatory to the tutorial without changing the YAML format or original puzzle. A session-only `history` command lists earlier commands in either story. Tests were written and observed failing before the core additions, then the clean build and console playthroughs passed. README and plan were updated; no new commit was made. |

| “do the next step” | Worked on Step 9: reviewed code and dependencies, updated Jackson YAML to 2.22.4, added the finalized Java 25 module-import feature, split command dispatch, and fixed invalid global settings and invalid path handling with regression tests. An independent read-only review found no security issue or gameplay regression; its Java 25 concern was addressed during the review. The final clean build passed all 16 tests, both stories were exercised, and study-guide.md was added. Step 9 remains unchecked pending IDE Sonar analysis and personal competency practice. No commit was made. |

The user installed SonarQube for IDE and opened its report after automated IDE
shortcuts were unreliable. Hermes inspected the visible report: 586 findings,
including 578 in the supplied assignment HTML. It addressed the 8 project-code
findings (constant reuse, import order, assertion lambdas, dependency versions,
and a documented narrow suppression for intentional console output). The clean
build passed; a fresh Sonar report is still required to verify the findings clear.

The user clarified whether the supplied assignment HTML was required, then
requested its deletion. Hermes removed the optional HTML reference and updated
the plan to link to `assignment.md`; historical log entries remain unchanged.

## 4 November 2026 — Project inspection and Step 9 verification

| Prompt or request | Purpose and outcome |
| --- | --- |
| “look through this folder and report what you read” | Read project instructions, documentation, build configuration, source, tests, and both stories; reported implementation and remaining assignment gates without changing code. |
| “could you do step 9” | Repeated the clean build: all 16 tests passed. Verified console playthroughs of the extended tutorial, forest story, and unchanged assignment YAML. Checked dependency release metadata, Java toolchains, and JEP 511. An independent read-only review found no concrete security issue or logic regression. IntelliJ accessibility timed out, so a fresh Sonar report remains unverified; the user's unaided competency demonstration also remains outstanding. Updated the plan and this log; no application code changes or commits were made. |

The user then reported “report says no issues”. Recorded the Sonar result as
user-reported in the plan; Step 9 remains open only for the personal competency
demonstration.

The user asked why Jackson questions were being asked; explained that Jackson
maps YAML to Java objects through StoryLoader and that the questions related to
written-check preparation. The user then authorized committing the accumulated
work. The clean build passed again before committing.

## Story expansion and self-evaluation preparation

| Prompt or request | Purpose and outcome |
| --- | --- |
| “can you look through this repo to get context and instructions for yourself?” | Read the instructions, assignment, documentation, source, stories and tests. Reran all 16 existing tests successfully; no code changes were needed. |
| “could you write the selbsevaluation?” / “fill out the already existing md file” | Created a labelled draft on the Desktop, then filled the missing paragraphs in the existing Desktop template while keeping the user's original paragraph. Explained that the final reflection must be personally revised. |
| “check the selbsevalutation md file if i fullfilled all the requirements?” | Compared the user's revised Desktop text with the assignment. Counted 492 words in the four reflection paragraphs; identified the missing personal-pride statement, remaining template instructions, and final project-file placement. |
| Questions about the wardrobe, lantern, telescope and Narnia ending | Explained the key requirement, directional commands and short tutorial ending. Verified the lantern route with its focused test. |
| “could you make more? like right now it's very underwhelming” | Expanded the tutorial into The Clockmaker's Winter, continuing beyond Narnia with six locations, a telescope-dependent gate, an archive chest and a bell-tower restoration puzzle. Added regression playthroughs, preserving the original puzzle and YAML format. Updated the README with player guidance and a spoiler walkthrough. |

The user then requested a commit of the expansion; it was committed as
`b8be018` without pushing.

The user reported misleading fountain instructions and stale descriptions after
taking the coat or opening the gate, and requested consistency throughout the
game. Reproduced eight regression scenarios before fixing them. Added generic
state-aware arrival/room inspection through the existing YAML action format,
updated item, gate, chest, repair and post-ending descriptions, and made the
gear's exact location and commands explicit. Repeated actions now acknowledge
completed progress. Updated automated coverage and exercised a console route
with revisits and the new ending, plus room inspection in the forest story.

The user clarified that better guidance must not mean giving away solutions.
Replaced the over-explicit command sequences with environmental clues: matching
shapes, the cable linking fountain and tower, a heat-sensitive seal and the
keeper's incomplete notes. Kept state-aware descriptions and repeat-action
checks. Generic syntax help remains separate from puzzle clues; the optional
spoiler walkthrough stays in the README. Added a regression check against
walkthrough commands embedded in story messages.

The user reported that natural attempts such as ringing the rope or pulling
the hammer failed unhelpfully, and asked how to finish after repairing the tower.
Added rope inspection and both rope interactions alongside the existing bell
command, physical feedback for the inaccessible hammer, and explicit error
responses for unsupported pull/ring/use objects. Verified a console route using
invalid commands followed by ringing the rope and reaching the ending; all 29
tests passed. These changes have not yet been committed.

## Continue this log

After each meaningful AI assisted work session, add the date, a short summary of the prompt(s), and what changed or was learned. Include learning, debugging, review, and implementation prompts. Keep the summary accurate; do not invent prompts or claim work that has not been completed.
