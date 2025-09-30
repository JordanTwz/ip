package nami;

import java.util.ArrayList;
import java.util.List;

public class Nami {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Nami() {
        this.ui = new Ui();
        this.storage = new Storage("data", "nami.txt");
        this.tasks = new TaskList(storage.load());
    }

    public void run() {
        ui.showWelcome();
        boolean exit = false;
        while (!exit) {
            String input = ui.readCommand();
            if (input == null) break;
            try {
                Parser.Parsed p = Parser.parse(input);

                switch (p.cmd) {
                case "bye":
                    exit = true;
                    ui.showBye();
                    break;

                case "list":
                    ui.showList(tasks.asList());
                    break;

                case "mark":
                    ensureRange(p.index);
                    tasks.get(p.index - 1).mark();
                    storage.save(tasks.asList());
                    ui.showMarked(tasks.get(p.index - 1));
                    break;

                case "unmark":
                    ensureRange(p.index);
                    tasks.get(p.index - 1).unmark();
                    storage.save(tasks.asList());
                    ui.showUnmarked(tasks.get(p.index - 1));
                    break;

                case "todo": {
                    Task t = new ToDo(p.desc);
                    tasks.add(t);
                    storage.save(tasks.asList());
                    ui.showAdded(t, tasks.size());
                    break;
                }

                case "deadline": {
                    // p.dueDate is guaranteed non-null by Parser (yyyy-MM-dd)
                    Task t = new Deadline(p.desc, p.dueDate);
                    tasks.add(t);
                    storage.save(tasks.asList());
                    ui.showAdded(t, tasks.size());
                    break;
                }

                case "event": {
                    Task t = new Event(p.desc, p.from, p.to);
                    tasks.add(t);
                    storage.save(tasks.asList());
                    ui.showAdded(t, tasks.size());
                    break;
                }

                case "delete":
                    ensureRange(p.index);
                    Task removed = tasks.remove(p.index - 1);
                    storage.save(tasks.asList());
                    ui.showDeleted(removed, tasks.size());
                    break;
                }
            } catch (NamiException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void ensureRange(Integer idx) throws NamiException {
        if (idx == null) throw new NamiException("Please provide a task number.");
        if (idx <= 0) throw new NamiException("Task number must be a positive integer.");
        if (tasks.size() == 0) throw new NamiException("Your list is empty. Add a task first (e.g., todo read book).");
        if (idx > tasks.size()) {
            throw new NamiException("Task number " + idx + " is out of range (1.." + tasks.size() + ").");
        }
    }

    public static void main(String[] args) {
        new Nami().run();
    }
}
