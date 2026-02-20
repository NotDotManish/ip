package chiron;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of tasks.
 * Provides methods to add, remove, retrieve, and search for tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with an initial list of tasks.
     *
     * @param loaded The list of tasks to initialize with.
     */
    public TaskList(List<Task> loaded) {
        this.tasks = new ArrayList<>(loaded);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The Task at the specified index.
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Task index out of bounds";
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) throws ChironException {
        if (tasks.stream().anyMatch(t -> t.equals(task))) {
            throw new ChironException("This task already exists. Focus on what’s new.");
        }
        tasks.add(task);
    }

    /**
     * Removes the task at the specified index.
     *
     * @param index The zero-based index of the task to remove.
     * @return The removed Task.
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "Task index out of bounds for removal";
        return tasks.remove(index);
    }

    /**
     * Sets the done status of the task at the specified index.
     *
     * @param index The zero-based index of the task.
     * @param done  True to mark as done, false to mark as not done.
     */
    public void setDone(int index, boolean done) {
        tasks.get(index).setDone(done);
    }

    /**
     * Returns an unmodifiable view of the tasks list.
     *
     * @return An unmodifiable List of tasks.
     */
    public List<Task> asUnmodifiableList() {
        return List.copyOf(tasks);
    }

    /**
     * Finds tasks whose description contains the specified keyword.
     *
     * @param keyword The keyword to search for.
     * @return A list of matching tasks.
     */
    public List<Task> find(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .toList();
    }
}
