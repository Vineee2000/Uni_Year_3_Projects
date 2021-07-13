package Comms;

import Main.Client;
import UI.IOHandler;
import UI.UI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketHandler implements Runnable{
    private static final int port = 8888;
    private final IOHandler parent;
    private Scanner reader;
    private PrintWriter writer;
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private Socket socket;
    private final AtomicBoolean standingBy = new AtomicBoolean(false);

    public SocketHandler (IOHandler creator) {
        parent = creator;
    }

    @Override
    public void run() {
        while (!connected.get()) {
            try {
                if (!standingBy.get()) {
                    connect();
                }
                } catch(IOException e){
                    e.printStackTrace();

                }
            }
        while (connected.get()) {
            String line = reader.nextLine();
            String[] substrings = line.split(" ");
            serverSignalProcessing(substrings);
        }
    }

    private void serverSignalProcessing(String[] substrings) {
        switch (substrings[0]) {
            case CommCodes.CONN_ACK -> {
                Client.setId(Integer.parseInt(substrings[1]));
                parent.connected();
            }

            case CommCodes.STOCK_BESTOW -> {
                parent.setLastKnownStockOwner(Client.getId());
                parent.displayMessage(UI.Messages.STOCK_BESTWOED);
            }

            case CommCodes.TRADERS_UPDATE -> {
                String tradersListRaw = substrings[1];
                tradersListRaw = tradersListRaw.substring(1, tradersListRaw.length()-1);
                String[] tradersArray = tradersListRaw.split(",");
                parent.setKnownTraders(tradersArray);
                parent.setLastKnownStockOwner(Integer.parseInt(substrings[2]));
                parent.showKnownTraders();
            }

            case CommCodes.REQ_DENIED -> {
                String errorString = "This action was denied by the server\n";
                switch (substrings[1]) {
                    case CommCodes.DenyCodes.NO_LOGIN -> errorString = errorString + "because you are not logged in yet. \n" +
                            "Please try to connect to the server again";
                    case CommCodes.DenyCodes.REDUN_LOGIN -> errorString = errorString + "You are already logged in";

                    case CommCodes.DenyCodes.NOT_INT -> errorString = errorString +
                            "Something other than a whole number was provided to the server when not expected";

                    case CommCodes.DenyCodes.NOT_OWNER -> {
                        errorString = errorString + "You are not the current stock owner";
                        writer.println(CommCodes.UPDATE_REQ);
                    }

                    case CommCodes.DenyCodes.ID_IS_NOT_CONNECTED ->
                            errorString = errorString + "Provided ID is not currently connected";

                    case CommCodes.DenyCodes.MISSING_DATA -> errorString = errorString + "Missing data in request";

                    case CommCodes.DenyCodes.UNKNOWN_MESSAGE ->
                            errorString = errorString + "We have send an incorrect message";

                    default -> errorString = errorString + "Unrecognized error code";
                }
                parent.displayError(errorString);
            }
        }
    }

    public synchronized void connect() throws IOException {
        try {
            socket = new Socket("localhost", port);
            reader = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(CommCodes.CONN_REQ + " " + Client.getId());
            connected.set(true);
        } catch (ConnectException e) {
            parent.displayMessage(UI.Messages.CONN_FAIL);
            standingBy.set(true);
        }
    }

    public void giveStock(int recipientId) {
        writer.println(CommCodes.STOCK_GIVE + " " + recipientId);
        parent.displayMessage(UI.Messages.STOCK_GIVE_ACK);
    }

    public void requestUpdate() {
        writer.println(CommCodes.UPDATE_REQ);
    }

    public void exitStandby() {
        standingBy.set(false);
    }
    public void disconnect() {
        writer.println(CommCodes.DISCONNECT);
    }
}
