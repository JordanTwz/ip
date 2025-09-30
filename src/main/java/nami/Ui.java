package nami;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner sc = new Scanner(System.in);

    public String readCommand() {
        return sc.hasNextLine() ? sc.nextLine() : null;
    }

    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Nami");
        System.out.println(" What can I do for you?");
        showLine();
    }

    public void showBye() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    public void showLine() { System.out.println(LINE); }

    public void showError(String msg) {
        showLine();
        System.out.println(" " + msg);
        showLine();
    }

    public void showList(List<Task> tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    public void showAdded(Task t, int count) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + count + " tasks in the list.");
        showLine();
    }

    public void showMarked(Task t) {
        showLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + t);
        showLine();
    }

    public void showUnmarked(Task t) {
        showLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        showLine();
    }

    public void showDeleted(Task t, int count) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + count + " tasks in the list.");
        showLine();
    }
}
