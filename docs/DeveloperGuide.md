# Developer Guide

## Design

### Architecture

The system consists of the following components:
* `Chiron`: The main entry point of the app.
* `Ui`: Handles user interaction and GUI.
* `Parser`: Interprets the user's commands.
* `Storage`: Reads and writes data to the disk.
* `TaskList`: Manages the lists of tasks.

## Implementation

### Adding Tasks

When the user enters a command to add a task, the `Parser` identifies the task type and passes the parameters. The `TaskList` acts as the in-memory registry, and `Storage` propagates it to disk. 

```java
// Logic flow for registration
Task t = new Todo(description);
tasks.add(t);
storage.save(tasks);
```
