package UI;

import Comms.CommsHandler;

import java.util.Set;

public class IOHandler {
    private final CommsHandler commsHandler;
    private UI thisUI;

    public IOHandler(CommsHandler comms, UIType uiType) {
        commsHandler = comms;
        commsHandler.passIo(this);
        if (uiType == UIType.TERMINAL) {
            new Thread(thisUI = new TerminalUI(this)).start();
        }
    }

    public void displayError(String errorDesc) {
        thisUI.displayError(errorDesc);
    }

    public void displayMessage(String message) {
        thisUI.displayMessage(message);
    }

    void toggleDebug() {
        commsHandler.toggleDebug();
    }

    void askForCurrentTraders() {
        commsHandler.displayCurrentTraders();
    }

    public void displayCurrentTraders(Set<Integer> traders, int stockHolder) {
        thisUI.displayCurrentTraders(traders, stockHolder);
    }

    void askForAllTraders() {
        commsHandler.displayKnownTraders();
    }

    public void displayAllKnownTraders(Set<Integer> traders) {
        thisUI.displayAllKnownTraders(traders);
    }

    void exit() {
        System.exit(0);
    }

    public enum UIType {
        TERMINAL,
        GUI
    }
}
