import java.util.Scanner;

public class Nami {
    private static final String LINE =
            "________________________________________________";

    public static void main(String[] args) {
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
            }
            System.out.println(LINE);
            System.out.println(" " + input);
            System.out.println(LINE);
        }
    }
}