import java.util.Scanner;

public class Nami {
    private static final String LINE =
            "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Task[] tasks = new Task[MAX_TASKS];
        int count = 0;

        Scanner sc = new Scanner(System.in);
        System.out.println(LINE);
        System.out.println("Hello! I'm Nami");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        while (sc.hasNextLine()) {
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println(LINE);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            } else if (input.equals("list")) {
                System.out.println(LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    Task t = tasks[i];
                    System.out.println(" " + (i + 1) + ".[" + t.getStatusIcon() + "] " + t.getDescription());
                }
                System.out.println(LINE);
            } else if (input.startsWith("mark ")) {
                int idx = parseIndex(input);
                if (idx >= 1 && idx <= count) {
                    tasks[idx - 1].mark();
                    System.out.println(LINE);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("   [" + tasks[idx - 1].getStatusIcon() + "] " + tasks[idx - 1].getDescription());
                    System.out.println(LINE);
                }
            } else if (input.startsWith("unmark ")) {
                int idx = parseIndex(input);
                if (idx >= 1 && idx <= count) {
                    tasks[idx - 1].unmark();
                    System.out.println(LINE);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("   [" + tasks[idx - 1].getStatusIcon() + "] " + tasks[idx - 1].getDescription());
                    System.out.println(LINE);
                }
            } else {
                if (count < MAX_TASKS) {
                    tasks[count++] = new Task(input);
                    System.out.println(LINE);
                    System.out.println("added: " + input);
                    System.out.println(LINE);
                }
            }
        }
    }

    private static int parseIndex(String input) {
        try {
            String[] parts = input.split("\\s+");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return -1;
        }
    }
}
