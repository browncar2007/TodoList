// Caronn Brown
// 10/9/2025
// CSE 122
// P0: Stonks
// TA: Katharine Zhang

// Class Comment: This program allows users to manage a to-do list by 
// adding, marking, loading, and saving tasks.
// If the priority extension is enabled, users can assign priority levels to tasks
// and have them automatically sorted.
// The program continuously displays the current to-do List until the user chooses to quit.
import java.util.*;
import java.io.*;
public class TodoListManager {
    public static final boolean EXTENSION_FLAG = true;

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> prioLevels = new ArrayList<>();
        List<String> todoList = new ArrayList<>();
        Scanner console = new Scanner(System.in);
        String choice = "";

        System.out.println("Welcome to your TODO List Manager!");

        while (!choice.equalsIgnoreCase("q")) {
            choice = menuOptions(console);

            if (choice.equalsIgnoreCase("a")) {
                if (EXTENSION_FLAG) {
                    prioritizeInput(console, todoList, prioLevels);
                    prioritizeItems(todoList, prioLevels);
                } else {
                    addItem(console, todoList);
                }
            } else if (choice.equalsIgnoreCase("m")) {
                markItemAsDone(console, todoList, prioLevels);
            } else if (choice.equalsIgnoreCase("l")) {
                loadItems(console, todoList, prioLevels);
            } else if (choice.equalsIgnoreCase("s")) {
                saveItems(console, todoList);
            } else if (!choice.equalsIgnoreCase("q")) {
                System.out.println("Unknown input: " + choice);
            }

            if (!choice.equalsIgnoreCase("q")) {
                printTodos(todoList);
            }
        }
    }

    // B: Prints all TODO items currently in the list. If list is empty,
    // displays a relaxation message.
    // E: None
    // R: None
    // P: todos – List of strings representing current TODO items.  
    public static void printTodos(List<String> todos) {
        System.out.println("Today's TODOs:");
        if (todos.isEmpty()) {
            System.out.println("  You have nothing to do yet today! Relax!");
        } else {
            for (int i = 0; i < todos.size(); i++) {
                int num = i + 1;
                System.out.println("  " + num + ": " + todos.get(i));
            }
        }
    }

    // B: Prompts user to add a new TODO item to the list.
    // Allows placement at a specific position.
    // E: NumberFormatException – if user input for index is not a valid integer.
    // R: None
    // P: console – Scanner to read user input.
    //    todos – List storing all current TODO items.
    public static void addItem(Scanner console, List<String> todos) {
        System.out.print("What would you like to add? ");
        String add = console.nextLine();

        if (!todos.isEmpty()) {
            int todoSize = todos.size() + 1;
            System.out.print("Where in the list should it be (" + 1 + "-" + todoSize + ")? ");
            System.out.print("(Enter for end): ");
            String index = console.nextLine();

            if (!index.isEmpty()) {
                int intIndex = Integer.parseInt(index) - 1;
                todos.add(intIndex, add);
            } else {
                todos.add(add);
            }
        } else {
            todos.add(add);
        }
    }

    // B: Allows user to mark a TODO item as completed. Removes it from the list.
    //    If priority sorting is active, also removes corresponding priority level.
    // E: NumberFormatException – if user input is not an integer.
    // R: None
    // P: console – Scanner to read user input.
    //    todos – List containing TODO items.
    //    prioLevels – List of integer priority levels corresponding to TODO items.
    public static void markItemAsDone(Scanner console, List<String> todos,
            List<Integer> prioLevels) {
        if (!todos.isEmpty()) {
            System.out.print("Which item did you complete (" + 1 + "-" + todos.size() + ")? ");
            String mark = console.nextLine();
            int index = Integer.parseInt(mark) - 1;

            if (EXTENSION_FLAG) {
                prioLevels.remove(index);
            }
            todos.remove(index);
        } else {
            System.out.println("All done! Nothing left to mark as done!");
        }
    }

    // B: Loads TODO items (and priorities if extension is enabled) from a specified file.
    //    Clears existing list before loading new data.
    // E: FileNotFoundException – if user specifies a file that cannot be opened.
    // R: None
    // P: console – Scanner to read file name input.
    //    todos – List storing TODO items.
    //    prioLevels – List storing priority levels for each item.
    public static void loadItems(Scanner console, List<String> todos,
            List<Integer> prioLevels) throws FileNotFoundException {
        System.out.print("File name? ");
        File file = new File(console.nextLine());
        Scanner fileScan = new Scanner(file);

        todos.clear();
        prioLevels.clear();

        while (fileScan.hasNextLine()) {
            String listItem = fileScan.nextLine();

            if (EXTENSION_FLAG) {
                Scanner tokenScan = new Scanner(listItem);
                String stringPrio = tokenScan.next();
                int intPrio = Integer.parseInt(stringPrio.substring(1, 2));
                prioLevels.add(intPrio);
                todos.add(listItem);
                prioritizeItems(todos, prioLevels);
            } else {
                todos.add(listItem);
            }
        }
    }

    // B: Saves all current TODO items to a user-specified file.
    // E: FileNotFoundException – if the output file cannot be created or opened.
    // R: None
    // P: console – Scanner to read file name input.
    //    todos – List of TODO items to be written to file.
    public static void saveItems(Scanner console, List<String> todos)
            throws FileNotFoundException {
        System.out.print("File name? ");
        File file = new File(console.nextLine());
        PrintStream out = new PrintStream(file);

        for (String todo : todos) {
            out.println(todo);
        }
    }

    // B: Displays menu options and returns user’s choice of operation.
    // E: None
    // R: Returns a string representing the user’s input choice.
    // P: console – Scanner to read user input.
    public static String menuOptions(Scanner console) {
        System.out.println("What would you like to do?");
        System.out.print("(A)dd TODO, (M)ark TODO as done, (L)oad TODOs, (S)ave TODOs, (Q)uit? ");
        return console.nextLine();
    }

    // B: Prompts user to input a TODO item with a priority level (1–5). Adds both to their lists.
    // E: NumberFormatException – if priority entered is not a valid integer.
    // R: None
    // P: console – Scanner to read user input.
    //    todos – List storing TODO items.
    //    prioLevels – List storing integer priority values.
    public static void prioritizeInput(Scanner console, List<String> todos,
        List<Integer> prioLevels) {
        System.out.print("What would you like to add? ");
        String add = console.nextLine();
        System.out.print("What priority level should it be (1-5)? ");
        int intPriority = Integer.parseInt(console.nextLine());

        prioLevels.add(intPriority);
        todos.add("[" + intPriority + "] " + add);
    }

    // B: Sorts TODO items and their associated priority levels in ascending order of priority.
    //    Reorders both lists to keep them synchronized.
    // E: None
    // R: None
    // P: todos – List of TODO items (formatted with priority tags).
    //    prioLevels – List of integer priorities corresponding to each TODO item.
    public static void prioritizeItems(List<String> todos, List<Integer> prioLevels) {
        for (int i = 1; i < todos.size(); i++) {
            if (prioLevels.get(i) < prioLevels.get(i - 1)) {
                int prio = prioLevels.get(i);
                String todoItem = todos.get(i);
                prioLevels.remove(i);
                todos.remove(i);
                prioLevels.add(i - 1, prio);
                todos.add(i - 1, todoItem);
                i = 0;
            }
        }
    }
}