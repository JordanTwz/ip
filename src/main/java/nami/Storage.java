package nami;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
/**
 * Persistence layer that loads/saves tasks to a UTF-8 text file.
 * Deadline dates use ISO format (yyyy-MM-dd) when available.
 */
public class Storage {
    private final Path dir;
    private final Path file;

    public Storage(String dirName, String fileName) {
        this.dir = Paths.get(dirName);
        this.file = this.dir.resolve(fileName);
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(dir)) Files.createDirectories(dir);
            if (!Files.exists(file)) {
                Files.createFile(file);
                return tasks;
            }
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length < 3) continue;
                String type = parts[0].trim();
                boolean done = "1".equals(parts[1].trim());
                String desc = parts[2].trim();

                Task t;
                switch (type) {
                case "T":
                    t = new ToDo(desc);
                    break;
                case "D": {
                    String byField = parts.length > 3 ? parts[3].trim() : "";
                    LocalDate date = null;
                    try { date = LocalDate.parse(byField); } catch (DateTimeParseException ignored) { }
                    t = (date != null) ? new Deadline(desc, date) : new Deadline(desc, byField);
                    break;
                }
                case "E": {
                    String from = parts.length > 3 ? parts[3].trim() : "";
                    String to   = parts.length > 4 ? parts[4].trim() : "";
                    t = new Event(desc, from, to);
                    break;
                }
                default:
                    continue;
                }
                if (done) t.mark();
                tasks.add(t);
            }
        } catch (IOException ignored) { }
        return tasks;
    }

    public void save(List<Task> tasks) {
        try {
            if (!Files.exists(dir)) Files.createDirectories(dir);
            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                if (t == null) continue;
                if (t instanceof ToDo) {
                    lines.add(String.format("T | %d | %s",
                            t.getStatusIcon().equals("X") ? 1 : 0, t.getDescription()));
                } else if (t instanceof Deadline) {
                    Deadline d = (Deadline) t;
                    lines.add(String.format("D | %d | %s | %s",
                            t.getStatusIcon().equals("X") ? 1 : 0, d.getDescription(), d.getBy()));
                } else if (t instanceof Event) {
                    Event e = (Event) t;
                    lines.add(String.format("E | %d | %s | %s | %s",
                            t.getStatusIcon().equals("X") ? 1 : 0, e.getDescription(), e.getFrom(), e.getTo()));
                }
            }
            Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ignored) { }
    }
}
