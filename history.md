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

## Continue this log

After each meaningful AI assisted work session, add the date, a short summary of the prompt(s), and what changed or was learned. Include learning, debugging, review, and implementation prompts. Keep the summary accurate; do not invent prompts or claim work that has not been completed.
