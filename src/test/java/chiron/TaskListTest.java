package chiron;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the TaskList class.
 */
public class TaskListTest {

    /**
     * Tests adding a task to the list.
     */
    @Test
    public void add_task_success() {
        // Verify that adding a task increases the list size
        TaskList tasks = new TaskList();
        assertEquals(0, tasks.size());

        tasks.add(new Todo("read book"));
        assertEquals(1, tasks.size());
    }

    /**
     * Tests deleting a task from the list.
     */
    @Test
    public void delete_task_success() {
        // Verify that removing a task decreases list size and returns the correct task
        TaskList tasks = new TaskList();
        tasks.add(new Todo("task 1"));
        tasks.add(new Todo("task 2"));
        assertEquals(2, tasks.size());

        Task removed = tasks.remove(0);
        assertEquals("task 1", removed.getDescription());
        assertEquals(1, tasks.size());
    }

    /**
     * Tests retrieving a task from the list.
     */
    @Test
    public void get_task_success() {
        // Verify retrieving a task by index
        TaskList tasks = new TaskList();
        Task t = new Todo("test task");
        tasks.add(t);

        assertEquals(t, tasks.get(0));
    }
}
