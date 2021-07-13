package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class TerminalUI extends UI {
    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    TerminalUI(IOHandler creator) {
        super(creator);
    }

    @Override
    public void run() {
        System.out.println("Welcome to the server.\n" +
                "Type \"help\" for a list of commands");
        while (true) {
            try {
                String input = console.readLine();
                String[] substrings = input.split(" ");
                switch (substrings[0].toLowerCase()) {
                    case "help" -> System.out.println(
                            "debug to toggle outputting of messages recieved by the server \n" +
                            "traders or list to see the list of currently connected traders\n" +
                            "known, history or all to see the list of all traders ever connected\n" +
                            "exit to shut the server down"
                    );

                    case "debug" -> parent.toggleDebug();

                    case "traders", "list" -> parent.askForCurrentTraders();

                    case "known", "history", "all" -> parent.askForAllTraders();

                    case "exit" -> parent.exit();

                    case "" -> {}

                    default -> System.out.println("Unknown command");
                }
            } catch (IOException e) {
                System.out.println("Error occurred while processing your command. Please try again");
            }

        }
    }

    @Override
    protected void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }

    @Override
    void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    void displayCurrentTraders(Set<Integer> traders, int stockHolder) {
        System.out.println("Current connections: \n" + traders.toString());
        System.out.println("Stock holder: " + stockHolder);
    }

    @Override
    void displayAllKnownTraders(Set<Integer> traders) {
        System.out.println("Here the IDs of all the traders that have ever connected to the server: ");
        System.out.println(traders.toString());
    }
}
