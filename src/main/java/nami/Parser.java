package nami;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
/**
 * Parses raw user input into a structured {@link Parsed} command object.
 * Throws {@link NamiException} with friendly messages for invalid input.
 */
public class Parser {

    /**
     * Parses one line of user input and captures argument data for downstream handling.
     *
     * @param input Raw text entered by the user.
     * @return A populated {@link Parsed} record describing the command and arguments.
     * @throws NamiException When the command is unknown or fails validation.
     */
    public static Parsed parse(String input) throws NamiException {
        String trimmedInput = sanitizeInput(input);

        String[] tokens = trimmedInput.split("\\s+", 2);
        String commandWord = tokens[0];
        String arguments = tokens.length > 1 ? tokens[1].trim() : "";

        switch (commandWord) {
        case "bye":
        case "list":
            ensureNoArguments(commandWord, arguments);
            return new Parsed(commandWord);

        case "mark":
        case "unmark":
        case "delete":
            return parseIndexCommand(commandWord, arguments);

        case "todo":
            return parseTodo(arguments, commandWord);

        case "deadline":
            return parseDeadline(arguments, commandWord);

        case "event":
            return parseEvent(arguments, commandWord);

        case "find":
            return parseFind(arguments, commandWord);

        default:
            throw new NamiException("I'm sorry, I don't know what that means :-(");
        }
    }

    /**
     * Validates that a space-separated argument string contains exactly one positive integer.
     *
     * @param s   The argument substring to validate.
     * @param cmd Command word for error messaging context.
     * @throws NamiException if the argument is missing, malformed, or out of range.
     */
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

    /**
     * Collapses repeating whitespace and trims leading/trailing spaces.
     *
     * @param s Text to normalise.
     * @return String with internal whitespace collapsed to single spaces.
     */
    private static String normalizeSpaces(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    private static String sanitizeInput(String input) throws NamiException {
        if (input == null) {
            throw new NamiException("Please enter a command.");
        }
        String trimmed = input.strip();
        if (trimmed.isEmpty()) {
            throw new NamiException("Please enter a command.");
        }
        return trimmed;
    }

    private static void ensureNoArguments(String commandWord, String arguments) throws NamiException {
        if (!arguments.isEmpty()) {
            throw new NamiException("'" + commandWord + "' does not take any arguments.");
        }
    }

    private static Parsed parseIndexCommand(String commandWord, String arguments) throws NamiException {
        int index = parseSinglePositiveInteger(arguments, commandWord);
        Parsed parsedCommand = new Parsed(commandWord);
        parsedCommand.index = index;
        return parsedCommand;
    }

    private static Parsed parseTodo(String arguments, String commandWord) throws NamiException {
        if (arguments.isEmpty()) {
            throw new NamiException("The description of a todo cannot be empty. Try: " + commandWord + " read book");
        }
        Parsed parsedCommand = new Parsed(commandWord);
        parsedCommand.desc = normalizeSpaces(arguments);
        return parsedCommand;
    }

    private static Parsed parseDeadline(String arguments, String commandWord) throws NamiException {
        int markerIndex = arguments.indexOf("/by");
        if (markerIndex < 0) {
            throw new NamiException("Deadline needs '/by'. Try: " + commandWord + " return book /by 2019-10-15");
        }
        String descriptionPart = arguments.substring(0, markerIndex).trim();
        if (descriptionPart.isEmpty()) {
            throw new NamiException("Deadline needs a description before /by.");
        }
        String datePart = arguments.substring(markerIndex + 3).trim();
        if (datePart.isEmpty()) {
            throw new NamiException("Deadline needs a date after /by. Use yyyy-MM-dd (e.g., 2019-10-15).");
        }

        Parsed parsedCommand = new Parsed(commandWord);
        parsedCommand.desc = normalizeSpaces(descriptionPart);
        parsedCommand.by = datePart; // keep raw for storage compatibility paths
        try {
            parsedCommand.dueDate = LocalDate.parse(datePart); // ISO yyyy-MM-dd
        } catch (DateTimeParseException ex) {
            throw new NamiException("Use date format yyyy-MM-dd (e.g., 2019-10-15).");
        }
        return parsedCommand;
    }

    private static Parsed parseEvent(String arguments, String commandWord) throws NamiException {
        int fromMarker = arguments.indexOf("/from");
        int toMarker = arguments.indexOf("/to", fromMarker >= 0 ? fromMarker + 5 : 0);
        if (fromMarker < 0 || toMarker < 0) {
            throw new NamiException("Event needs '/from' and '/to'. Try: " + commandWord + " meeting /from Mon 2pm /to 4pm");
        }

        String descriptionPart = arguments.substring(0, fromMarker).trim();
        if (descriptionPart.isEmpty()) {
            throw new NamiException("Event needs a description before /from.");
        }

        String fromPart = arguments.substring(fromMarker + 5, toMarker).trim();
        if (fromPart.isEmpty()) {
            throw new NamiException("Event needs a /from time.");
        }

        String toPart = arguments.substring(toMarker + 3).trim();
        if (toPart.isEmpty()) {
            throw new NamiException("Event needs a /to time.");
        }

        Parsed parsedCommand = new Parsed(commandWord);
        parsedCommand.desc = normalizeSpaces(descriptionPart);
        parsedCommand.from = normalizeSpaces(fromPart);
        parsedCommand.to = normalizeSpaces(toPart);
        return parsedCommand;
    }

    private static Parsed parseFind(String arguments, String commandWord) throws NamiException {
        if (arguments.isEmpty()) {
            throw new NamiException("Please provide a keyword. Try: " + commandWord + " book");
        }
        Parsed parsedCommand = new Parsed(commandWord);
        parsedCommand.keyword = normalizeSpaces(arguments); // allow multi-word phrase
        return parsedCommand;
    }

    private static int parseSinglePositiveInteger(String arguments, String commandWord) throws NamiException {
        requireSinglePositiveInteger(arguments, commandWord);
        return Integer.parseInt(arguments.trim());
    }

    /**
     * Immutable command payload produced by {@link Parser#parse(String)}.
     * Fields default to empty/zero and are populated depending on the command.
     */
    public static class Parsed {
        public final String cmd;
        public String desc = "";
        public String by   = "";
        public String from = "";
        public String to   = "";
        public Integer index = null;
        public LocalDate dueDate = null;   // Level-8
        public String keyword = "";        // Level-9

        public Parsed(String cmd) { this.cmd = cmd; }
    }
}
