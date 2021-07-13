package Main;

import UI.IOHandler;

public class Client {
    private static int id;

    public static void main(String[] args) {
        IOHandler.UIType uiType = IOHandler.UIType.TERMINAL;
        for (String s: args) {
            uiType = switch (s) {
                case "-gui" -> IOHandler.UIType.GUI;
                case "-terminal" -> IOHandler.UIType.TERMINAL;
                default -> uiType;
            };
        }
        IOHandler ioHandler = new IOHandler(uiType);
    }

    public static void setId(int newID) {
        id = newID;
    }

    public static int getId() {
        return id;
    }
}
