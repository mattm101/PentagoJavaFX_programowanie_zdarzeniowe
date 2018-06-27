package malecmateusz.pentago.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class NetworkController implements  ConnectorListener{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static NetworkController instance = null;

    private List<NetworkListener> networkListeners= new ArrayList<NetworkListener>();
    private List<DataListener> dataListeners= new ArrayList<DataListener>();
    private Connector connector = null;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    private NetworkController(){}

    public static NetworkController getInstance() {
        if(instance == null) instance = new NetworkController();
        return instance;
    }

    public void disconnect(){
        LOGGER.info("NetworkController - zamykanie połączenia");
        connector.closeCrap();
    }

    public void connect(){
        LOGGER.info("NetworkController - otwieranie połączenia");
        connector = new Connector(this);
        executorService.execute(connector);
    }

    public void sendData(DataProtocol data) throws UnableToSendException{
        try {
            connector.sendData(data);
        } catch (IOException e) {
            LOGGER.warning("Nie udało się wysłać danych do serwera");
           throw new UnableToSendException(data);
        }
    }

    @Override
    public void notifyAboutConnectorEvent(ConnectorEvent e) {
        switch (e.getEventType()){
            case ConnectorEvent.CONNECTED:
                fireOnConnected();
                break;

            case ConnectorEvent.FAILED_TO_CONNECT:
                fireOnConnectionFailure();
                break;

            case ConnectorEvent.DISCONNECTED:
                fireOnDisconnected();
                break;

            case ConnectorEvent.DATA_RECEIVED:
                fireOnDataReceived(e.getMessage());
                break;
        }
    }

    public synchronized void addNetworkListener(NetworkListener listener){
        networkListeners.add(listener);
    }

    public synchronized void removeNetworkListener(NetworkListener listener){
        networkListeners.remove(listener);
    }

    public synchronized void addDataListener(DataListener listener){
        dataListeners.add(listener);
    }

    public synchronized void removeDataListener(DataListener listener){
        dataListeners.remove(listener);
    }

    public synchronized void fireOnConnected(){
        for(NetworkListener l : networkListeners){
            l.onConnected();
        }
    }

    public synchronized void fireOnDisconnected(){
        for(NetworkListener l : networkListeners){
            l.onDosconnected();
        }
    }

    public synchronized void fireOnConnectionFailure(){
        for(NetworkListener l : networkListeners){
            l.onConnectionFailure();
        }
    }

    public synchronized void fireOnDataReceived(DataProtocol data){
        for(DataListener l : dataListeners){
            l.onDataRecieved(data);
        }
    }
}
