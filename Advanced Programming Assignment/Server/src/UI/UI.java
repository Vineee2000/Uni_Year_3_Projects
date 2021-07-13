package UI;

import java.util.ArrayList;
import java.util.Set;

public abstract class UI implements Runnable{
    final IOHandler parent;

    UI(IOHandler creator) {
        parent = creator;
    }

    protected void displayError(String errorMessage) {
    }

    void displayMessage(String message) {
    }

    void displayCurrentTraders(Set<Integer> traders, int stockHolder) {
    }

    void displayAllKnownTraders(Set<Integer> traders) {
    }
}
