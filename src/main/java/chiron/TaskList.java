package chiron;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> loaded) {
        this.tasks = new ArrayList<>(loaded);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public void setDone(int index, boolean done) {
        tasks.get(index).setDone(done);
    }

    public List<Task> asUnmodifiableList() {
        return List.copyOf(tasks);
    }

    public List<Task> find(String keyword) {
        return tasks.stream()
                .filter(task -> task.description().contains(keyword))
                .toList();
    }
}
