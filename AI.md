# AI usage in Chiron

Throughout the development of the Chiron chatbot, various AI technologies were leveraged to accelerate coding, guarantee codebase stability, troubleshoot bugs, and design UI elements. This document logs the usage of these tools as part of the `A-AiAssisted` increment.

## Code Generation & Issue Resolution
**Tool used:** Antigravity (Powered by Google Deepmind)

Antigravity was given access as an agent to traverse the local project directory and fulfill requirements across weeks 5 and 6, and it was used to accomplish the following tasks:

1. **Bug fixing and Linter adherence**: Checkstyle violations in legacy files like `Storage.java` and `Parser.java` were autonomously parsed, diagnosed, and corrected by Antigravity directly running terminal commands, replacing file contents, and re-running `./gradlew build` iteratively until the terminal output passed.
2. **Refactoring**: Antigravity efficiently identified code that needed to be abstracted, helping extract the `Ui` class for a much cleaner architecture.
3. **Complex Logic**: For the `A-MoreErrorHandling` increment, the tool was utilized to traverse `TaskList.java` and implement Java Stream API functions to prevent duplicate task registrations, and check Temporal access to prevent impossible event time pairings.

## GUI Upgrades & Aesthetics
**Tool used:** Antigravity & Image Generation

The `A-BetterGui` framework and stylistic aesthetic transformations were conceptualized and refined alongside an AI Assistant.
- **Micro-Animations & CSS:** Deepmind's agent correctly built out an asymmetric interaction view where User messages aligned left and Chiron responses mirrored to the right. Profile images were masked to perfect spheres using `Circle` clipping directly in `DialogBox.java` avoiding messy FXML constraints.
- **Debugging JavaFX resizing issues:** AI assistance quickly solved layout clipping using `AnchorPane.setBottomAnchor` mapping when transitioning from a static JavaFX frame to a dynamic, resizable GUI.
- **Asset Procurement:** An AI generated, tailored digital parchment texture `scroll1.jpg` used meticulously for the JavaFX text-input background, creating a high-fidelity Camp Half-Blood immersion. 

## Documentation
**Tool used:** Antigravity 
Antigravity generated the `docs/README.md` file using proper strictly formatted GitHub Flavored Markdown (GFMD) including syntax examples and structured headers.

By relying on agentic AI capabilities for repetitive logic debugging and structural UI implementations, the development trajectory could primarily center around app-architecture and software engineering fundamentals—an exceptionally productive pipeline!
