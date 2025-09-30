package nami;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public static Parsed parse(String input) throws NamiException {
        if (input == null) throw new NamiException("Please enter a command.");
        String trimmed = input.strip();
        if (trimmed.isEmpty()) throw new NamiException("Please enter a command.");

        String[] head = trimmed.split("\\s+", 2);
        String cmd = head[0];
        String rest = head.length > 1 ? head[1].trim() : "";

        Parsed p = new Parsed(cmd);

        switch (cmd) {
        case "bye":
        case "list":
            // treat extra arguments as a user mistake
            if (!rest.isEmpty()) {
                throw new NamiException("'" + cmd + "' does not take any arguments.");
            }
            return p;

        case "mark":
        case "unmark":
        case "delete":
            // must be exactly one positive-integer token
            requireSinglePositiveInteger(rest, cmd);
            p.index = Integer.parseInt(rest);
            return p;

        case "todo":
            if (rest.isEmpty()) {
                throw new NamiException("The description of a todo cannot be empty. Try: todo read book");
            }
            p.desc = normalizeSpaces(rest);
            return p;

        case "deadline": {
            // Level-8: require "<desc> /by yyyy-MM-dd" and parse to LocalDate
            int byIdx = rest.indexOf("/by");
            if (byIdx < 0) {
                throw new NamiException("Deadline needs '/by'. Try: deadline return book /by 2019-10-15");
            }
            String[] parts = rest.split("\\s+/by\\s+", 2);
            p.desc = normalizeSpaces(parts[0]);
            if (p.desc.isEmpty()) {
                throw new NamiException("Deadline needs a description before /by.");
            }
            if (parts.length < 2 || parts[1].isBlank()) {
                throw new NamiException("Deadline needs a date after /by. Use yyyy-MM-dd (e.g., 2019-10-15).");
            }
            String dateText = parts[1].trim();
            try {
                p.dueDate = LocalDate.parse(dateText); // ISO yyyy-MM-dd
            } catch (DateTimeParseException ex) {
                throw new NamiException("Use date format yyyy-MM-dd (e.g., 2019-10-15).");
            }
            // keep by string for backward-compatibility if callers still read it
            p.by = dateText;
            return p;
        }

        case "event": {
            // unchanged (Level-8 minimal only requires deadlines to use dates)
            if (!rest.contains("/from") || !rest.contains("/to")) {
                throw new NamiException("Event needs '/from' and '/to'. Try: event meeting /from Mon 2pm /to 4pm");
            }
            String[] p1 = rest.split("\\s+/from\\s+", 2);
            p.desc = normalizeSpaces(p1[0]);
            if (p1.length < 2) {
                throw new NamiException("Event needs a /from time. Try: event meeting /from Mon 2pm /to 4pm");
            }
            String[] p2 = p1[1].split("\\s+/to\\s+", 2);
            p.from = p2[0].trim();
            p.to   = (p2.length > 1) ? p2[1].trim() : "";
            if (p.desc.isEmpty())  throw new NamiException("Event needs a description before /from.");
            if (p.from.isEmpty())  throw new NamiException("Event /from time cannot be empty.");
            if (p.to.isEmpty())    throw new NamiException("Event needs a /to time.");
            return p;
        }

        default:
            throw new NamiException("I'm sorry, I don't know what that means :-(");
        }
    }

    private static void requireSinglePositiveInteger(String s, String cmd) throws NamiException {
        if (s == null || s.isEmpty()) {
            throw new NamiException("Please provide a task number. Try: " + cmd + " 2");
        }
        String[] tokens = s.split("\\s+");
        if (tokens.length != 1) {
            throw new NamiException("Only one number is allowed. Try: " + cmd + " 2");
        }
        String n = tokens[0];
        if (!n.matches("\\d+")) {
            throw new NamiException("Task number must be a positive integer. Try: " + cmd + " 2");
        }
        try {
            Integer.parseInt(n);
        } catch (NumberFormatException e) {
            throw new NamiException("Task number is too large. Try a smaller positive integer.");
        }
    }

    private static String normalizeSpaces(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    public static class Parsed {
        public final String cmd;
        public String desc = "";
        public String by   = "";       // kept for backward-compat
        public String from = "";
        public String to   = "";
        public Integer index = null;
        public LocalDate dueDate = null;   // <-- Level-8 field

        public Parsed(String cmd) { this.cmd = cmd; }
    }
}