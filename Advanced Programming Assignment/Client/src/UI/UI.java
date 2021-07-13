package UI;

import java.util.TreeSet;

public abstract class UI implements Runnable{
    final IOHandler parent;

    public UI(IOHandler creator) {
        parent = creator;
    }

    int requestLogin(boolean retry) {
        return 0;
    }

    void connected() {
    }

    void displayError(String errorMessage) {

    }

    void displayMessage(Messages message) {

    }

    void displayString(String string) {

    }

    void displayTraders(TreeSet<Integer> traderIdList) {

    }

    void sendStock(int targetId) {

    }

    void displayStockOwner() {

    }

    public enum Messages {
        STOCK_GIVE_ACK,
        NOT_STOCK_OWNER,
        STOCK_BESTWOED,
        CONN_FAIL
    }
}
