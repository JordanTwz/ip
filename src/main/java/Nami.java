import java.util.Scanner;

public class Nami {
    private static final String LINE =
            "________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        String[] tasks = new String[MAX_TASKS];
        int count = 0;

        Scanner sc = new Scanner(System.in);
        System.out.println(LINE);
        System.out.println("Hello! I'm Nami.");
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
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(LINE);
            } else {
                // adding a task
                if (count < MAX_TASKS) {
                    tasks[count++] = input;
                    System.out.println(LINE);
                    System.out.println("added: " + input);
                    System.out.println(LINE);
                }
            }
        }
    }
}