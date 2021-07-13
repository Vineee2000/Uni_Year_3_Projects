package Comms;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final CommsHandler parent;
    private Scanner scanner;
    private PrintWriter writer;
    private int traderId = -1;
    private final AtomicBoolean connected = new AtomicBoolean(true);
    private boolean authorised = false;
    private AtomicBoolean debug = new AtomicBoolean();

    protected ClientHandler(CommsHandler parent, Socket clientSocket) {
        socket = clientSocket;
        this.parent = parent;
        try {
        scanner = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void run() {
        while (connected.get()) {
            try {
                String line = scanner.nextLine();
                String[] substrings = line.split(" ");
                if (!authorised) {
                    authorize(substrings);
                } else {
                    processClientInput(substrings);
                }
            } catch (NoSuchElementException elementException) {
                    parent.disconnectClient(traderId);
            }
        }
        if (!connected.get()) {
            Thread.currentThread().interrupt();
        }
    }

    private void processClientInput(String[] substrings) {
        try {
            if (debug.get()) {
                parent.displayMessage(Arrays.toString(substrings));
            }
            switch (substrings[0]) {
                case CommCodes.CONN_REQ -> writer.println(CommCodes.REQ_DENIED + " " + CommCodes.DenyCodes.REDUN_LOGIN);

                case CommCodes.STOCK_GIVE -> handleStockGive(substrings[1]);

                case CommCodes.UPDATE_REQ -> parent.updateSingleClient(traderId);

                case CommCodes.DISCONNECT -> connected.set(false);

                default -> writer.println(CommCodes.REQ_DENIED + " " + CommCodes.DenyCodes.UNKNOWN_MESSAGE);
            }
        } catch (Exception e) {
            writer.println(CommCodes.REQ_DENIED + " " + null);
        }
    }

    private void authorize(String[] substrings) {
        try {
            if (CommCodes.CONN_REQ.equals(substrings[0])) {
                try {
                    traderId = Integer.parseInt(substrings[1]);
                    traderId = parent.newConnectionId(traderId);
                    boolean isFirst = parent.addVerifiedConnection(traderId, this);
                    if (isFirst) {
                        receiveStock();
                    }
                    authorised = true;
                    writer.println(CommCodes.CONN_ACK + " " + traderId);
                    parent.propagateStockOwner();
                } catch (NumberFormatException notInt) {
                    writer.println(CommCodes.REQ_DENIED + " " + CommCodes.DenyCodes.NOT_INT);
                } catch (ArrayIndexOutOfBoundsException noData) {
                    writer.println(CommCodes.REQ_DENIED + " " + CommCodes.DenyCodes.MISSING_DATA);
                }
            } else {
                writer.println(CommCodes.REQ_DENIED + " " + CommCodes.DenyCodes.NO_LOGIN);
            }
        } catch (Exception e) {
            writer.println(CommCodes.REQ_DENIED + " " + null);
        }
    }

    private void handleStockGive(String substring) {
        try {
            int recipientId = Integer.parseInt(substring);
            if (parent.checkIdPresent(recipientId)) {
                parent.transferStock(traderId, recipientId);
            }
            else {
                writer.println(CommCodes.REQ_DENIED + " " +CommCodes.DenyCodes.ID_IS_NOT_CONNECTED);
            }
        } catch (NumberFormatException numberFormatException)  {
            writer.println(CommCodes.REQ_DENIED + " " + CommCodes.DenyCodes.NOT_INT);
        } catch (Exception e) {
            writer.println(CommCodes.REQ_DENIED + " " + null);
        }
    }

    protected void receiveStock() {
        writer.println(CommCodes.STOCK_BESTOW);
    }

    protected void denyNotOwner() {
        writer.println(CommCodes.REQ_DENIED + " " + CommCodes.DenyCodes.NOT_OWNER);
    }

    protected void traderListUpdate(String traderList, String stockHolder) {
        writer.println(CommCodes.TRADERS_UPDATE + " " + traderList + " " + stockHolder);
    }

    void setToKill() {
        connected.set(false);
    }

    void toggleDebug() {
        debug.set(!debug.get());
    }
}
