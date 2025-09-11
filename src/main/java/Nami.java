import java.util.Scanner;

public class Nami {
    private static final String LINE =
            "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Task[] tasks = new Task[MAX_TASKS];
        int count = 0;

        printGreeting();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String raw = sc.nextLine();
            if (raw == null) break;
            String input = raw.trim();

            // ignore blank lines quietly (no output change)
            if (input.isEmpty()) {
                continue;
            }

            if (input.equals("bye")) {
                printExit();
                break;
            } else if (input.equals("list")) {
                // list tasks
                printLine();
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                printLine();

            } else if (input.startsWith("mark")) {
                // mark <index>
                Integer idx = parseIndex(input, "mark");
                if (idx == null) {
                    // detailed error already printed by parseIndex
                    continue;
                }
                if (idx < 1 || idx > count) {
                    printError("Task number " + idx + " is out of range (1.." + count + ").");
                    continue;
                }
                tasks[idx - 1].mark();
                printLine();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[idx - 1]);
                printLine();

            } else if (input.startsWith("unmark")) {
                // unmark <index>
                Integer idx = parseIndex(input, "unmark");
                if (idx == null) {
                    continue;
                }
                if (idx < 1 || idx > count) {
                    printError("Task number " + idx + " is out of range (1.." + count + ").");
                    continue;
                }
                tasks[idx - 1].unmark();
                printLine();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[idx - 1]);
                printLine();

            } else if (input.equals("todo") || input.startsWith("todo ")) {
                // todo <desc>
                String desc = (input.length() > 4) ? input.substring(5).trim() : "";
                if (desc.isEmpty()) {
                    printError("The description of a todo cannot be empty. Try: todo read book");
                    continue;
                }
                Task t = new ToDo(desc);
                count = add(tasks, count, t);

            } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                // deadline <desc> /by <when>
                String rest = (input.length() > 8) ? input.substring(9).trim() : "";
                if (rest.isEmpty()) {
                    printError("Deadline needs a description and /by. Try: deadline return book /by Sunday");
                    continue;
                }
                String[] parts = rest.split("\\s+/by\\s+", 2);
                String desc = parts[0].trim();
                if (desc.isEmpty()) {
                    printError("Deadline needs a description before /by. Try: deadline return book /by Sunday");
                    continue;
                }
                if (parts.length < 2 || parts[1].trim().isEmpty()) {
                    printError("Deadline needs a /by time. Try: deadline return book /by Sunday");
                    continue;
                }
                String by = parts[1].trim();
                Task t = new Deadline(desc, by);
                count = add(tasks, count, t);

            } else if (input.equals("event") || input.startsWith("event ")) {
                // event <desc> /from <start> /to <end>
                String rest = (input.length() > 5) ? input.substring(6).trim() : "";
                if (rest.isEmpty()) {
                    printError("Event needs a description, /from and /to. Try: event meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                String[] p1 = rest.split("\\s+/from\\s+", 2);
                String desc = p1[0].trim();
                if (desc.isEmpty()) {
                    printError("Event needs a description before /from. Try: event project meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                if (p1.length < 2 || p1[1].trim().isEmpty()) {
                    printError("Event needs a /from time. Try: event meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                String[] p2 = p1[1].split("\\s+/to\\s+", 2);
                String from = p2[0].trim();
                if (from.isEmpty()) {
                    printError("Event /from time cannot be empty. Try: event meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                if (p2.length < 2 || p2[1].trim().isEmpty()) {
                    printError("Event needs a /to time. Try: event meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                String to = p2[1].trim();
                Task t = new Event(desc, from, to);
                count = add(tasks, count, t);

            } else {
                // unknown command
                printError("I'm sorry, I don't know what that means :-(");
            }
        }
    }

    /**
     * Parses an index for commands like "mark 2" or "unmark 3".
     * Returns null and prints a specific error if missing or invalid.
     */
    private static Integer parseIndex(String input, String cmdName) {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            printError("Please provide a task number. Try: " + cmdName + " 2");
            return null;
        }
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            printError("Task number must be an integer. Try: " + cmdName + " 2");
            return null;
        }
    }

    private static int add(Task[] tasks, int count, Task t) {
        if (count < MAX_TASKS) {
            tasks[count++] = t;
            printLine();
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + t);
            System.out.println(" Now you have " + count + " tasks in the list.");
            printLine();
        } else {
            printError("Task list is full (" + MAX_TASKS + "). Consider removing some tasks.");
        }
        return count;
    }

    private static void printError(String msg) {
        System.out.println(LINE);
        System.out.println(" " + msg);
        System.out.println(LINE);
    }

    private static void printLine() {
        System.out.println(LINE);
    }

    private static void printGreeting() {
        printLine();
        System.out.println(" Hello! I'm Nami");
        System.out.println(" What can I do for you?");
        printLine();
    }

    private static void printExit() {
        printLine();
        System.out.println(" Bye. Hope to see you again soon!");
        printLine();
    }
}
