package nami;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Nami {
    private static final String LINE =
            "____________________________________________________________";

    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>();

        printGreeting();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String raw = sc.nextLine();
            if (raw == null) break;
            String input = raw.trim();

            if (input.isEmpty()) {
                continue; // ignore blank lines
            }

            if (input.equals("bye")) {
                printExit();
                break;

            } else if (input.equals("list")) {
                printLine();
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));
                }
                printLine();

            } else if (input.startsWith("mark")) {
                Integer idx = parseIndex(input, "mark");
                if (idx == null) continue;
                if (!inRange(idx, tasks.size())) {
                    printError("Task number " + idx + " is out of range (1.." + tasks.size() + ").");
                    continue;
                }
                tasks.get(idx - 1).mark();
                printLine();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks.get(idx - 1));
                printLine();

            } else if (input.startsWith("unmark")) {
                Integer idx = parseIndex(input, "unmark");
                if (idx == null) continue;
                if (!inRange(idx, tasks.size())) {
                    printError("Task number " + idx + " is out of range (1.." + tasks.size() + ").");
                    continue;
                }
                tasks.get(idx - 1).unmark();
                printLine();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks.get(idx - 1));
                printLine();

            } else if (input.equals("todo") || input.startsWith("todo ")) {
                String desc = (input.length() > 4) ? input.substring(5).trim() : "";
                if (desc.isEmpty()) {
                    printError("The description of a todo cannot be empty. Try: todo read book");
                    continue;
                }
                Task t = new ToDo(desc);
                tasks.add(t);
                printLine();
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + t);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                printLine();

            } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                String rest = (input.length() > 8) ? input.substring(9).trim() : "";
                if (rest.isEmpty()) {
                    printError("Deadline needs a description and /by. Try: deadline return book /by Sunday");
                    continue;
                }
                String[] parts = rest.split("\\s+/by\\s+", 2);
                String desc = parts[0].trim();
                if (desc.isEmpty()) {
                    printError("Deadline needs a description before /by.");
                    continue;
                }
                String by = (parts.length < 2 || parts[1].trim().isEmpty()) ? "" : parts[1].trim();
                Task t = new Deadline(desc, by);
                tasks.add(t);
                printLine();
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + t);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                printLine();

            } else if (input.equals("event") || input.startsWith("event ")) {
                String rest = (input.length() > 5) ? input.substring(6).trim() : "";
                if (rest.isEmpty()) {
                    printError("Event needs a description, /from and /to. Try: event meeting /from Mon 2pm /to 4pm");
                    continue;
                }
                String[] p1 = rest.split("\\s+/from\\s+", 2);
                String desc = p1[0].trim();
                if (desc.isEmpty()) {
                    printError("Event needs a description before /from.");
                    continue;
                }
                String from = "", to = "";
                if (p1.length > 1) {
                    String[] p2 = p1[1].split("\\s+/to\\s+", 2);
                    from = p2[0].trim();
                    to = (p2.length > 1) ? p2[1].trim() : "";
                }
                Task t = new Event(desc, from, to);
                tasks.add(t);
                printLine();
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + t);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                printLine();

            } else if (input.startsWith("delete")) {
                Integer idx = parseIndex(input, "delete");
                if (idx == null) continue;
                if (!inRange(idx, tasks.size())) {
                    printError("Task number " + idx + " is out of range (1.." + tasks.size() + ").");
                    continue;
                }
                Task removed = tasks.remove(idx - 1);
                printLine();
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + removed);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                printLine();

            } else {
                printError("I'm sorry, I don't know what that means :-(");
            }
        }
    }

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

    private static boolean inRange(int idx, int size) {
        return idx >= 1 && idx <= size;
    }

    private static void printError(String msg) {
        System.out.println(LINE);
        System.out.println(" " + msg);
        System.out.println(LINE);
    }

    private static void printLine() { System.out.println(LINE); }

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
