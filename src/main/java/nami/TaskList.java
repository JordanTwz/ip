package nami;

import java.util.ArrayList;
import java.util.List;
/**
 * In-memory list of tasks with basic operations and search.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Exposes the backing list so callers can iterate or persist tasks.
     * Mutating the returned list updates the underlying task store directly.
     */
    public List<Task> asList() { return tasks; }

    public int size() { return tasks.size(); }

    public Task get(int idx) { return tasks.get(idx); }

    public void add(Task t) { tasks.add(t); }

    public Task remove(int idx) { return tasks.remove(idx); }

    /**
     * Returns new list containing tasks whose description includes the keyword (case-insensitive).
     *
     * @param keyword Search term entered by the user.
     * @return Matching tasks in encounter order.
     */
    public List<Task> findByKeyword(String keyword) {
        String needle = keyword.toLowerCase();
        List<Task> out = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDescription().toLowerCase().contains(needle)) {
                out.add(t);
            }
        }
        return out;
    }
}
