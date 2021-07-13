package UI;

import Main.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

public class TerminalUI extends UI{
    private final BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

    public TerminalUI(IOHandler creator) {
        super(creator);
    }


    @Override
    public void run() {
        while (true) {
            try {
                String input = userInput.readLine();
                String[] substrings = input.split(" ");
                switch (substrings[0].toLowerCase()) {
                    case "help" -> System.out.println(
                            "traders or show to see all traders currently online\n" +
                            "send or give to send the stock to someone else if you possess it\n" +
                            "stock to see the current stock owner\n" +
                            "exit to terminate the program"
                    );

                    case "connect" -> parent.connect();

                    case "traders", "show" -> parent.updateKnownTraders();

                    case "send", "give" -> {
                        System.out.println("Enter the stock recipient ID");
                        recipientIdInputLoop(false);
                    }

                    case "stock" -> displayStockOwner();

                    case "exit" -> parent.exit();

                    case "" -> {}

                    default -> System.out.println("Unknown command");
                }
            } catch (IOException e) {
                System.out.println("Error occurred while processing your command. Please try again");
            }
        }
    }

    private void recipientIdInputLoop(boolean recurring) throws IOException {
        if (recurring) {
            System.out.println("Please enter a valid whole number");
        }
        String tempLine = userInput.readLine();
        tempLine.replaceAll("\\s", "");
        try {
            parent.sendStock(Integer.parseInt(tempLine));
        } catch (NumberFormatException e) {
            recipientIdInputLoop(true);
        }
    }

    @Override
    int requestLogin(boolean retry) {
        if (!retry) {
            System.out.println("Please enter your trader ID if you have traded with us before:");
        }
        try {
            String line = userInput.readLine();
            if (line.equals("")) {
                System.out.println("We will create a new ID for you");
                System.out.println("Type \"connect\" when you are ready to start, \"help\" for the list of all commands");
                return 0;
            }
            else {
                System.out.println("Type \"connect\" when you are ready to start, \"help\" for the list of all commands");
                return Integer.parseInt(line);
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try to enter your ID again, or just press Enter if this is your first time");
        }
        return -1;
    }

    @Override
    void connected() {
        System.out.println("Connection to the server established. Your ID is: " + Client.getId());
    }

    @Override
    void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }

    @Override
    void displayMessage(Messages message) {
        switch (message) {
            case STOCK_GIVE_ACK -> System.out.println("You have given your stock away");

            case NOT_STOCK_OWNER -> System.out.println("You are not the stock owner");

            case STOCK_BESTWOED -> System.out.println("You have just recieved the stock");

            case CONN_FAIL -> System.out.println("Connection to the server failed. Please try again later.");
        }
    }

    @Override
    void displayTraders(TreeSet<Integer> traderIdList) {
        System.out.println("Currently connected traders:");
        for (Integer traderId :
                traderIdList) {
            System.out.println(traderId.toString());
        }
        displayStockOwner();
        System.out.println("You are trader No. " + Client.getId());
    }

    @Override
    void displayStockOwner() {
        if (parent.getLastKnownStockOwner() == Client.getId()) {
            System.out.println("You own the stock");
        }
        else {
            System.out.println("Stock owner ID is: " + parent.getLastKnownStockOwner());
        }
    }

    @Override
    void sendStock(int targetId) {
       parent.sendStock(targetId);
    }
}
