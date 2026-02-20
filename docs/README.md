# Chiron User Guide

Chiron is a wise and patient desktop chatbot designed to help you manage your tasks. Be it a simple todo or an event you need to be present for, Chiron's camp-themed interface will help you bring order to your chaos. 

![Ui](Ui.png)

## Features

### Add a `todo`
Add a simple task without any date or time attached.
**Format:** `todo <description>` or `t <description>`
**Example:** `t Read chapter 4`

### Add a `deadline`
Add a task that needs to be done before a specific date and time.
**Format:** `deadline <description> /by yyyy-MM-dd [HHmm]` or `d <description> /by yyyy-MM-dd [HHmm]`
**Example:** `d Submit assignment /by 2024-05-15 2359`

### Add an `event`
Add a task that starts at a specific time and ends at a specific time.
**Format:** `event <description> /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]` or `e ...`
**Example:** `event Project meeting /from 2024-05-16 1400 /to 2024-05-16 1600`

### Display list of tasks
Displays all the tasks you currently have logged.
**Format:** `list` or `ls`

### Search for tasks
Search your list of tasks using a keyword.
**Format:** `find <keyword>` or `f <keyword>`
**Example:** `f meeting`

### Mark/unmark a task
Mark a specific task as done or not done using its index number.
**Format:** `mark <index>` or `m <index>` and `unmark <index>` or `um <index>`
**Example:** `m 1`

### Delete a task
Remove a specific task from the list permanently.
**Format:** `delete <index>` or `rm <index>`
**Example:** `delete 2`

### Exit the application
Close the chat window.
**Format:** `bye` or `b`

## FAQ

**Q: Do I need to be exact with the date and time format?**
A: Yes, precision matters. Chiron will only read dates in the `yyyy-MM-dd` format, and time in `HHmm` (24-hour time).