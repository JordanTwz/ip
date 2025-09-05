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
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                printExit();
                break;
            } else if (input.equals("list")) {
                printLine();
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                printLine();
            } else if (input.startsWith("mark ")) {
                int idx = parseIndex(input);
                if (idx >= 1 && idx <= count) {
                    tasks[idx - 1].mark();
                    printLine();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[idx - 1]);
                    printLine();
                }
            } else if (input.startsWith("unmark ")) {
                int idx = parseIndex(input);
                if (idx >= 1 && idx <= count) {
                    tasks[idx - 1].unmark();
                    printLine();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[idx - 1]);
                    printLine();
                }
            } else if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                Task t = new ToDo(desc);
                count = add(tasks, count, t);
            } else if (input.startsWith("deadline ")) {
                String rest = input.substring(9).trim();
                String[] parts = rest.split("\\s+/by\\s+", 2);
                String desc = parts[0];
                String by = parts.length > 1 ? parts[1] : "";
                Task t = new Deadline(desc, by);
                count = add(tasks, count, t);
            } else if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                String[] p1 = rest.split("\\s+/from\\s+", 2);
                String desc = p1[0];
                String from = "", to = "";
                if (p1.length > 1) {
                    String[] p2 = p1[1].split("\\s+/to\\s+", 2);
                    from = p2[0];
                    to = (p2.length > 1) ? p2[1] : "";
                }
                Task t = new Event(desc, from, to);
                count = add(tasks, count, t);
            } else {
                // Level-1 echo fallback
                printLine();
                System.out.println(" " + input);
                printLine();
            }
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
        }
        return count;
    }

    private static int parseIndex(String input) {
        try {
            String[] parts = input.split("\\s+");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return -1;
        }
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
