package chiron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to a file.
 * Manages the storage file path and format conversion.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage handler.
     *
     * @param filePath The path to the storage file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return A list of tasks loaded from the file.
     * @throws ChironException If the file cannot be read or format is invalid.
     */
    public List<Task> load() throws ChironException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            assert tasks != null : "Loaded tasks list should not be null";
        } catch (FileNotFoundException e) {
            throw new ChironException("Storage file disappeared. Magic?");
        }

        return tasks;
    }

    /**
     * Saves the current list of tasks to the storage file.
     *
     * @param tasks The list of tasks to save.
     * @throws ChironException If saving fails (e.g., IO error).
     */
    public void save(TaskList tasks) throws ChironException {
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                boolean created = file.getParentFile().mkdirs();
                if (!created && !file.getParentFile().exists()) {
                    throw new ChironException("Failed to create directory for storage.");
                }
            }

            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks.asUnmodifiableList()) {
                writer.write(task.toSaveString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new ChironException("Failed to save tasks. Amnesia sets in.");
        }
    }

    /**
     * Parses a line from the storage file into a Task object.
     * Format: T | 0/1 | desc
     * D | 0/1 | desc | by
     * E | 0/1 | desc | from | to
     *
     * @param line The line string to parse.
     * @return The parsed Task, or null if parsing fails.
     */
    private Task parseLine(String line) {
        // Expected format:
        // T | 0/1 | desc
        // D | 0/1 | desc | yyyy-MM-dd [HHmm]
        // E | 0/1 | desc | yyyy-MM-dd [HHmm] | yyyy-MM-dd [HHmm]
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean done = "1".equals(parts[1].trim());
        String desc = parts[2].trim();

        Task t;
        switch (type) {
        case "T":
            t = new Todo(desc);
            break;
        case "D":
            if (parts.length < 4) {
                return null;
            }
            Parser.ParsedDateTime by = Parser.parseDateTime(parts[3].trim());
            if (by == null) {
                return null;
            }
            t = new Deadline(desc, by.value(), by.hasTime());
            break;
        case "E":
            if (parts.length < 5) {
                return null;
            }
            Parser.ParsedDateTime from = Parser.parseDateTime(parts[3].trim());
            Parser.ParsedDateTime to = Parser.parseDateTime(parts[4].trim());
            if (from == null || to == null) {
                return null;
            }
            t = new Event(desc, from.value(), from.hasTime(), to.value(), to.hasTime());
            break;
        default:
            return null;
        }

        t.setDone(done);
        return t;
    }
}
