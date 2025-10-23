package nami;
/**
 * Base type for all tasks, tracking description and completion state.
 */
public abstract class Task {
    protected final String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() { this.isDone = true; }
    public void unmark() { this.isDone = false; }
    public boolean isDone() { return isDone; }
    public String getStatusIcon() { return isDone ? "X" : " "; }
    public String getDescription() { return description; }

    @Override
    public abstract String toString(); // subclasses format [T]/[D]/[E]
}
