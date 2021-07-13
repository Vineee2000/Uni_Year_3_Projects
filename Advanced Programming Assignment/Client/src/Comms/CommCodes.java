package Comms;

class CommCodes {
    static final String CONN_REQ = "CONN_REQ";
    static final String CONN_ACK = "CONN_ACK";
    static final String STOCK_GIVE = "STOCK_GIVE_REQ";
    static final String STOCK_BESTOW = "STOCK_BESTOW";
    static final String TRADERS_UPDATE = "TRADERS_UPDATE";
    static final String UPDATE_REQ = "UPDATE_REQ";
    static final String DISCONNECT = "DISCONNECT";

    static final String REQ_DENIED = "REQ_DENIED";

    static class DenyCodes {
        static final String NO_LOGIN = "NO_LOGIN";
        static final String REDUN_LOGIN = "REDUN_LOGIN";
        static final String NOT_INT = "NOT_INT";
        static final String NOT_OWNER = "NOT_OWNER";
        static final String ID_IS_NOT_CONNECTED = "ID_IS_NOT_CONNECTED";
        static final String MISSING_DATA = "MISSING_DATA";
        static final String UNKNOWN_MESSAGE = "UNKNOWN_MESSAGE";
    }
}
