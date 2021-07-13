import Comms.CommsHandler;
import UI.IOHandler;

public class Server {

    public static void main(String[] args) {
        CommsHandler commsHandler;
        Thread thread = new Thread(commsHandler = new CommsHandler());
        IOHandler.UIType uiType = IOHandler.UIType.TERMINAL;
        for (String s: args) {
            uiType = switch (s) {
                case "-gui" -> IOHandler.UIType.GUI;
                case "-terminal" -> IOHandler.UIType.TERMINAL;
                default -> uiType;
            };
        }
        IOHandler ioHandler = new IOHandler(commsHandler, uiType);
        thread.start();
    }

}

