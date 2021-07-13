package Comms;

import UI.IOHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class CommsHandler implements Runnable{
    private final static int port = 8888;
    private IOHandler ioHandler;
    private final TreeSet<Integer> knownTraders = new TreeSet<>();
    private final HashMap<Integer, ClientHandler> currentConnections = new HashMap<>();
    private int stockHolderId;
    private final String homeDir;

    public CommsHandler() {
        String os = System.getProperty("os.name").toUpperCase();
        if (os.contains("WINDOWS")) {
            homeDir = System.getenv("AppData");
        }
        else {
            homeDir = System.getProperty("user.home");
        }

        File file = new File(homeDir + "/MyServer/knownTraders.txt");
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",");
            while (scanner.hasNext()) {
                knownTraders.add(scanner.nextInt());
            }
        } catch (FileNotFoundException e) {
            File folder = new File(homeDir + "/MyServer");
            folder.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                ioHandler.displayError("Failed to retrieve or create known traders log file.");
            }
        }
    }

    public void passIo(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, clientSocket);
                new Thread(clientHandler, "Client Handler").start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean addVerifiedConnection(int clientId, ClientHandler clientHandler) {
        boolean isFirst = currentConnections.isEmpty();
        if (isFirst) {
            stockHolderId = clientId;
        }
        currentConnections.put(clientId, clientHandler);
        return isFirst;
    }

    protected int newConnectionId(int traderId) {
        if (currentConnections.containsKey(traderId)) {
            traderId = generateNewId();
        }
        if (traderId == 0) {
            return generateNewId();
        }
        else {
            knownTraders.add(traderId);
            return traderId;
        }
    }

    private int generateNewId() {
        if (knownTraders.isEmpty()) {
            knownTraders.add(1);
            return 1;
        }
        else {
            int newId = knownTraders.last() + 1;
            knownTraders.add(newId);
            return newId;
        }
    }

    private void giveStockRandom() {
        ArrayList<Integer> idArray = new ArrayList<>(currentConnections.keySet());
        int recipientId = idArray.get(new Random().nextInt(idArray.size()));
        try {
            currentConnections.get(recipientId).receiveStock();
        } catch (Exception e) {
            giveStockRandom();
        }
        stockHolderId = recipientId;
        propagateStockOwner();
    }

    boolean checkIdPresent(int id) {
        return currentConnections.containsKey(id);
    }

    protected synchronized void transferStock(int stockGiverId, int recipientId) {
        boolean transferVerified;
        transferVerified = verifyTransfer(stockGiverId, recipientId);
        if (transferVerified) {
            try {
                currentConnections.get(recipientId).receiveStock();
                stockHolderId = recipientId;
            }
            catch(Exception e) {
                try {
                    currentConnections.get(stockHolderId).receiveStock();
                } catch (Exception e1) {
                    giveStockRandom();
                }
            }
            propagateStockOwner();
        }
        else {
            currentConnections.get(stockGiverId).denyNotOwner();
        }
    }

    protected boolean verifyTransfer(int senderId, int recipientId) {
        return (senderId == stockHolderId && checkIdPresent(recipientId));
    }
    
    void propagateStockOwner() {
        String  tradersArray = Arrays.toString(currentConnections.keySet().toArray()).replaceAll("\\s", "");
        currentConnections.forEach((traderId, traderHandler) -> traderHandler.traderListUpdate(tradersArray, Integer.toString(stockHolderId)));
    }

    void updateSingleClient(int clientId) {
        String  tradersArray = Arrays.toString(currentConnections.keySet().toArray()).replaceAll("\\s", "");
        currentConnections.get(clientId).traderListUpdate(tradersArray, Integer.toString(stockHolderId));
    }

    void disconnectClient(int clientId) {
        currentConnections.get(clientId).setToKill();
        currentConnections.remove(clientId);
        recordId(clientId);
        if ((clientId == stockHolderId) && !currentConnections.isEmpty()) {
            giveStockRandom();
        }
    }

    void recordId(int id) {
        try {
            FileWriter fileWriter = new FileWriter(homeDir + "/MyServer/knownTraders.txt", true);
            fileWriter.write(id + ",");
            fileWriter.close();
        } catch (FileNotFoundException e) {
            File folder = new File(homeDir + "/MyServer");
            folder.mkdirs();
            try {
                new File(homeDir + "/MyServer/knownTraders.txt").createNewFile();
            } catch (IOException ioException) {
                ioHandler.displayError("Failed to retrieve or create known traders log file.");
            }
        } catch (IOException e) {
            ioHandler.displayError("Failed to record known traders");
        }
    }

    void displayMessage(String message) {
        ioHandler.displayMessage(message);
    }

    public void toggleDebug() {
        currentConnections.forEach((traderId, clientHandler) -> clientHandler.toggleDebug());
    }

    public void displayCurrentTraders() {
        ioHandler.displayCurrentTraders(currentConnections.keySet(), stockHolderId);
    }

    public void displayKnownTraders() {
        ioHandler.displayAllKnownTraders(knownTraders);
    }
}
