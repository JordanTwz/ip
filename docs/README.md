# Nami: Text UI Task Assistant

Nami is a simple command-line chatbot that helps you track tasks.
It supports **todos**, **deadlines (with real dates)**, **events**, **listing**, **mark/unmark**, **delete**, **find**, and **auto-save** to disk.

> Requires **Java 17+**.

---

## Getting Started

### 1) Run Nami from a JAR

1. Download the released JAR from this repo’s **Releases** page (your instructor may provide it).
2. Put the JAR in an **empty folder** (recommended).
3. Open a terminal in that folder and run:

   ```bash
   java -jar "nami.jar"
   ```

   You should see:

   ```
   ____________________________________________________________
    Hello! I'm Nami
    What can I do for you?
   ____________________________________________________________
   ```

### 2) Where your data is saved

* Nami saves to a **relative** file: `./data/nami.txt` (next to where you run the JAR).
* The `data/` folder and `nami.txt` are created automatically on first run.
* Data is **updated automatically** after you add, delete, mark, or unmark a task.

---

## Usage Rules (important)

* **Commands are case-sensitive**: use lowercase (`todo`, `deadline`, `event`, `list`, `mark`, `unmark`, `delete`, `find`, `bye`).
* **Task numbering is 1-based**: the first task is `1`, not `0`.
* **Deadlines must use date format** `yyyy-MM-dd` (e.g., `2019-10-15`).
  Nami displays them as `MMM d yyyy` (e.g., `Oct 15 2019`).
* **Events** accept free-text times; they only require `/from` and `/to` parts (no date parsing).
* Extra arguments to commands that don’t take any (e.g., `list now`) are rejected with a friendly error.

---

## Commands

### `todo DESC`

Adds a todo task.

**Example**

```
todo read book
```

**Output**

```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
```

---

### `deadline DESC /by yyyy-MM-dd`

Adds a deadline task with a real date.

**Example**

```
deadline return book /by 2019-10-15
```

**Output**

```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
```

**Notes**

* Input must be **`yyyy-MM-dd`**. If the format is wrong, Nami explains the expected format.
* The stored file uses ISO dates (e.g., `2019-10-15`).

---

### `event DESC /from START /to END`

Adds an event with a start and end (free text).

**Example**

```
event project meeting /from Mon 2pm /to 4pm
```

**Output**

```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

---

### `list`

Shows all tasks in order.

**Example**

```
list
```

**Output**

```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

---

### `mark INDEX`  /  `unmark INDEX`

Marks or unmarks the task at the given 1-based index.

**Example**

```
mark 2
```

**Output**

```
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Oct 15 2019)
____________________________________________________________
```

**Example**

```
unmark 2
```

**Output**

```
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Oct 15 2019)
____________________________________________________________
```

---

### `delete INDEX`

Deletes the task at the given 1-based index.

**Example**

```
delete 3
```

**Output**

```
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 2 tasks in the list.
____________________________________________________________
```

---

### `find KEYWORD`

Shows tasks whose **description** contains the keyword (case-insensitive).

**Example**

```
find book
```

**Output**

```
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________
```

> Note: Search matches the **description text** only (not dates/times).

---

### `bye`

Exits the app.

**Output**

```
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

---

## Error Messages (what you’ll see and why)

* **Empty input**

  ```
  Please enter a command.
  ```

* **Extra arguments for a command that takes none**

  ```
  'list' does not take any arguments.
  ```

* **Missing or invalid index (mark/unmark/delete)**

  ```
  Please provide a task number. Try: mark 2
  ```

  ```
  Task number must be a positive integer. Try: mark 2
  ```

  ```
  Task number 5 is out of range (1..2).
  ```

  If the list is empty:

  ```
  Your list is empty. Add a task first (e.g., todo read book).
  ```

* **Todo without a description**

  ```
  The description of a todo cannot be empty. Try: todo read book
  ```

* **Deadline without `/by` or with wrong date format**

  ```
  Deadline needs '/by'. Try: deadline return book /by 2019-10-15
  ```

  ```
  Deadline needs a date after /by. Use yyyy-MM-dd (e.g., 2019-10-15).
  ```

  ```
  Use date format yyyy-MM-dd (e.g., 2019-10-15).
  ```

* **Event missing parts**

  ```
  Event needs '/from' and '/to'. Try: event meeting /from Mon 2pm /to 4pm
  ```

  ```
  Event /from time cannot be empty.
  ```

  ```
  Event needs a /to time.
  ```

* **Unknown command**

  ```
  I'm sorry, I don't know what that means :-(
  ```

---

## Data File Format (`data/nami.txt`)

Each line is one task, pipe-separated:

```
T | 1 | read book
D | 0 | return book | 2019-10-15
E | 0 | project meeting | Mon 2pm | 4pm
```

* First column: `T` (Todo), `D` (Deadline), `E` (Event)
* Second column: `1` = done, `0` = not done
* For deadlines, the last column is an **ISO date** `yyyy-MM-dd`.
* For events, the last two columns are free-text `/from` and `/to`.

The file is overwritten on each save (UTF-8).

---

## Tips

* Commands are **strict**: use the exact keywords and flags shown above.
* Deadlines only parse the **date**; events do **not** parse times.
* Use `list` often to confirm indices before `mark`, `unmark`, or `delete`.

---

## Example Session

```
____________________________________________________________
 Hello! I'm Nami
 What can I do for you?
____________________________________________________________

todo borrow book
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________

deadline return book /by 2019-10-15
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________

find book
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
____________________________________________________________

bye
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

---