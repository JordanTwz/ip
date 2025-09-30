package nami;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public List<Task> asList() { return tasks; }

    public int size() { return tasks.size(); }

    public Task get(int idx) { return tasks.get(idx); }

    public void add(Task t) { tasks.add(t); }

    public Task remove(int idx) { return tasks.remove(idx); }

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
