package chiron;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to a file.
 */
public class Storage {
    private final String dirName;
    private final String fileName;

    /**
     * Constructs a Storage instance.
     *
     * @param dirName  The name of the directory to store data in.
     * @param fileName The name of the file to store data in.
     */
    public Storage(String dirName, String fileName) {
        this.dirName = dirName;
        this.fileName = fileName;
    }

    private File dir() {
        return new File("./" + dirName);
    }

    private File file() {
        return new File("./" + dirName + "/" + fileName);
    }

    /**
     * Loads tasks from the storage file.
     * If the file does not exist, returns an empty list.
     *
     * @return A list of tasks loaded from the file.
     * @throws ChironException If the file cannot be read properly.
     */
    public List<Task> load() throws ChironException {
        ArrayList<Task> loaded = new ArrayList<>();
        File f = file();

        if (!f.exists()) {
            return loaded; // first run: no file yet
        }

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task t = parseLine(line);
                if (t != null) {
                    loaded.add(t);
                }
            }
        } catch (FileNotFoundException e) {
            return loaded;
        }
        return loaded;
    }

    /**
     * Saves the current list of tasks to the storage file.
     * Creates the directory and file if they do not exist.
     *
     * @param tasks The TaskList to save.
     * @throws ChironException If writing to the file fails.
     */
    public void save(TaskList tasks) throws ChironException {
        try {
            File d = dir();
            if (!d.exists()) {
                boolean ok = d.mkdir();
                if (!ok) {
                    throw new ChironException("I couldn’t create the data folder.");
                }
            }

            try (FileWriter fw = new FileWriter(file())) {
                for (Task t : tasks.asUnmodifiableList()) {
                    fw.write(t.toSaveString());
                    fw.write(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new ChironException("I couldn’t write your tasks to disk.");
        }
    }

    private Task parseLine(String line) {
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
                if (parts.length < 4)
                    return null;
                Parser.ParsedDateTime by = Parser.parseDateTime(parts[3].trim());
                if (by == null)
                    return null;
                t = new Deadline(desc, by.value(), by.hasTime());
                break;
            case "E":
                if (parts.length < 5)
                    return null;
                Parser.ParsedDateTime from = Parser.parseDateTime(parts[3].trim());
                Parser.ParsedDateTime to = Parser.parseDateTime(parts[4].trim());
                if (from == null || to == null)
                    return null;
                t = new Event(desc, from.value(), from.hasTime(), to.value(), to.hasTime());
                break;
            default:
                return null;
        }

        t.setDone(done);
        return t;
    }
}
