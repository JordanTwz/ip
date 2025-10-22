package nami;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Task with a due date. Stores both a parsed {@link LocalDate} for new entries
 * and a raw string so legacy save files remain readable.
 */
public class Deadline extends Task {
    private final LocalDate byDate;   // preferred in Level-8
    private final String byRaw;       // fallback for legacy data (pre-Level-8)

    /**
     * Creates a deadline backed by a parsed {@link LocalDate}.
     *
     * @param description Task description shown to the user.
     * @param byDate Parsed due date in ISO format.
     */
    public Deadline(String description, LocalDate byDate) {
        super(description);
        this.byDate = byDate;
        this.byRaw = null;
    }

    /**
     * Creates a deadline using the original raw string format.
     * Useful when loading older save files that did not parse dates.
     */
    public Deadline(String description, String byRaw) {
        super(description);
        this.byDate = null;
        this.byRaw = byRaw == null ? "" : byRaw;
    }

    /**
     * Returns the storage-ready representation: ISO text when available,
     * otherwise the legacy raw value.
     */
    public String getBy() {
        return byDate != null ? byDate.toString() : byRaw;
    }

    /**
     * Legacy accessor retained for compatibility with older code paths that expect it here.
     */
    @Override
    public String getDescription() { return description; }

    @Override
    public String toString() {
        String shown = (byDate != null)
                ? byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"))
                : byRaw;
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + shown + ")";
    }
}
