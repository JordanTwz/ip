package nami;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDate byDate;   // preferred in Level-8
    private final String byRaw;       // fallback for legacy data (pre-Level-8)

    public Deadline(String description, LocalDate byDate) {
        super(description);
        this.byDate = byDate;
        this.byRaw = null;
    }

    // Keep a raw-string constructor so old saves still load
    public Deadline(String description, String byRaw) {
        super(description);
        this.byDate = null;
        this.byRaw = byRaw == null ? "" : byRaw;
    }

    // ISO text for storage (yyyy-MM-dd) if date exists, else the raw text.
    public String getBy() {
        return byDate != null ? byDate.toString() : byRaw;
    }

    public String getDescription() { return description; } // if not already present in Task

    @Override
    public String toString() {
        String shown = (byDate != null)
                ? byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"))
                : byRaw;
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + shown + ")";
    }
}
