package UI;

import Comms.SocketHandler;
import Main.Client;

import java.util.HashSet;
import java.util.TreeSet;

public class IOHandler {
    private final UI thisUI;
    private SocketHandler socketHandler;
    private final TreeSet<Integer> knownTraders = new TreeSet<>();
    private int lastKnownStockOwner;

    public IOHandler(UIType uiType) {
        Thread thread;
        if (uiType == UIType.TERMINAL) {
            thread = new Thread(thisUI = new TerminalUI(this), "UI");
        }
        else {
            thread = new Thread(thisUI = new TerminalUI(this), "UI");
        }
        Client.setId(login());
        thread.start();
    }

    private int login() {
        int newId = thisUI.requestLogin(false);
        while (newId<0) {
            newId = thisUI.requestLogin(true);
        }
        return newId;
    }

    void connect() {
        if (socketHandler==null) {
            new Thread(socketHandler = new SocketHandler(this)).start();
        }
        else {
            socketHandler.exitStandby();
        }
    }

    public void connected() {
        thisUI.connected();
    }

    public void displayError(String errorDesc) {
        thisUI.displayError(errorDesc);
    }

    public void displayMessage(UI.Messages message) {
        thisUI.displayMessage(message);
    }

    void sendStock(int targetId) {
        if (Client.getId() == lastKnownStockOwner) {
            socketHandler.giveStock(targetId);
        }
        else {
            thisUI.displayMessage(UI.Messages.NOT_STOCK_OWNER);
        }
    }

    public int getLastKnownStockOwner() {
        return lastKnownStockOwner;
    }

    public void setLastKnownStockOwner(int lastKnownStockOwner) {
        this.lastKnownStockOwner = lastKnownStockOwner;
    }

    public void setKnownTraders(String [] tradersArray) {
        HashSet<Integer> intSet = new HashSet<>();
        for (String traderId :
                tradersArray) {
            intSet.add(Integer.parseInt(traderId));
        }
        knownTraders.clear();
        knownTraders.addAll(intSet);
    }

    public void showKnownTraders() {
        thisUI.displayTraders(knownTraders);
    }

    void updateKnownTraders() {
        socketHandler.requestUpdate();
    }

    void exit() {
        if (socketHandler != null) {
            socketHandler.disconnect();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.exit(1);
        }
        System.exit(0);
    }

    public enum UIType {
        TERMINAL,
        GUI
    }
}
