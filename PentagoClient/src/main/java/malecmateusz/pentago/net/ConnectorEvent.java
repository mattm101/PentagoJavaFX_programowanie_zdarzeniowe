package malecmateusz.pentago.net;

import java.util.EventObject;
/*
przenosi informację o stanie połączenia Connector do do NetworkController
 */
public class ConnectorEvent extends EventObject{
    public static final int CONNECTED = 0;
    public static final int DISCONNECTED = 1;
    public static final int DATA_RECEIVED = 2;
    public static final int FAILED_TO_CONNECT = 3;
    private final int eventType;
    private final DataProtocol message;
    public ConnectorEvent(Object source, final int eventType, DataProtocol message) {
        super(source);
        this.eventType = eventType;
        this.message = message;
    }

    public int getEventType(){
        return eventType;
    }

    public DataProtocol getMessage() {
        return message;
    }
}
