# AI Usage in Chiron

Throughout the development of the Chiron chatbot, I used AI tools (specifically Google's Gemini-based assistants like Antigravity) to act as a pair-programmer. While the core logic, application architecture, and primary design decisions were developed by me, AI was leveraged to speed up repetitive tasks, troubleshoot specific bugs, and refine UI aesthetics. This document logs the usage of these tools as part of the `A-AiAssisted` increment.

## 1. Code Quality & Linter Adherence
**How AI helped:** I used AI to help me quickly track down and fix stubborn Checkstyle violations.
**Details:** When the CI/CD pipeline failed due to hidden indentation issues in older legacy files like `Storage.java` and `Parser.java`, I used the AI assistant to parse the Checkstyle error logs and identify exactly which lines needed their spacing adjusted from 12 spaces to 8 spaces. This saved me a significant amount of manual formatting time.

## 2. GUI Upgrades & CSS Styling
**How AI helped:** I used AI to help me write the CSS for my JavaFX application and troubleshoot layout constraints.
**Details:** I designed the basic FXML layout and the camp-themed aesthetic I wanted (asymmetric chat bubbles, circular profile pictures, and a scrolling parchment background). I then prompted the AI to help me translate this design into standard JavaFX CSS (`main.css`). The AI provided the specific CSS rules to clip the `ImageView` into perfect circles and suggested using `AnchorPane` constraints to ensure the scroll pane resized correctly when the window was expanded.

## 3. Boilerplate & Documentation
**How AI helped:** I used AI to help generate the initial draft of my GitHub-flavored Markdown document.
**Details:** After I finished writing all the command logic, I provided the AI with a list of the features (e.g., `todo`, `event`, `deadline`, aliases like `t` and `m`) and asked it to format a clean, structured `README.md` file for the User Guide. I then reviewed and refined the generated documentation.

## Summary
Using AI as a supplementary tool allowed me to focus my efforts on the software engineering principles, the core logic of the application, and the overall design system, while outsourcing tedious formatting tasks, CSS syntax lookups, and boilerplate generation to the assistant.
