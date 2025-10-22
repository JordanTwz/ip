package nami;

/**
 * Task that spans a time range with free-form {@code from}/{@code to} fields.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task covering a given time range.
     *
     * @param description Description of the event.
     * @param from        Start time or marker supplied by the user.
     * @param to          End time or marker supplied by the user.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description + " (from: " + from + " to: " + to + ")";
    }
}
