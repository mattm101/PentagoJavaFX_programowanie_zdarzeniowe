package malecmateusz.pentago.net;

import com.google.gson.Gson;
import malecmateusz.pentago.config.ConfigManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/*
model w module sieciowym, to on łączy się z serwerem i wysyła dane do serwera,
parsując wysyłane obiekty do JSON'a
 */
public class Connector implements Runnable{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private List<ConnectorListener> listeners = new ArrayList<ConnectorListener>();
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private String serverIP = "";
    private int port = 6789;
    private Gson gson = new Gson();
    private final Object connectLock = new Object();
    private ConfigManager config;

    public Connector(ConnectorListener listener){
        try {
            config = ConfigManager.getInstance();
            port = Integer.valueOf((String)config.getValue("PORT"));
            LOGGER.info("Pobrano port serwera " + port);
        } catch (IOException e) {
            port = 6789;
            LOGGER.severe("Nie udało się pobrać portu serwera - ustawianie portu domyślnego " + port);
        }
        addListener(listener);
    }

    private void connectToServer() throws IOException {
            connection = new Socket(InetAddress.getByName(serverIP), port);
            LOGGER.info("Connector - Utworzono gniazdo z serwerem");
    }

    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        LOGGER.info("Connector - Otwarto streamy");
    }

    private void receiveData() throws EOFException, IOException {
        DataProtocol message = null;
        do {
            try {
                String msg = (String)input.readObject();
                message =  gson.fromJson(msg, DataProtocol.class);
                //tutaj wywoluje listenera networkControllera i przekazuje message
                fireConnectorEvent(new ConnectorEvent(this, ConnectorEvent.DATA_RECEIVED, message));
            }catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
        }while(connection != null && connection.isConnected());
    }

    public void sendData(DataProtocol message) throws IOException {
        String data = gson.toJson(message);
        synchronized (connectLock){
            output.writeObject(data);
            output.flush();
        }
    }

    public void closeCrap()  {

        try {
            if(connection!= null && connection.isConnected()) {
                input.close();
                output.close();
                connection.close();
            }
            LOGGER.info("Connector - Zamknięto połączenie z serwerem");
        } catch (IOException e) {
          LOGGER.warning("Connector - Połączenie z serwerem zostało zamknięte przed czyszczeniem gniazda");
        }
    }

    @Override
    public void run() {
        boolean isConnected = true;
        try {
            //TODO:czy tutaj moze byc problem z synchronizacją gdy rzuci wyjątkiem? czy zdejmie zamek?

                connectToServer();
                setupStreams();

            fireConnectorEvent(new ConnectorEvent(this, ConnectorEvent.CONNECTED, null));
        } catch (IOException e) {
            isConnected = false;
            fireConnectorEvent(new ConnectorEvent(this, ConnectorEvent.FAILED_TO_CONNECT, null));
           LOGGER.severe("Conncetor - Nie udało się połączyć z serwerem");
        }

        try {
            if(isConnected) receiveData();
        } catch (IOException e) {
            LOGGER.warning("Connector - Nie można odebrać danych z serwera");
        } finally {
            if(isConnected) {
                synchronized (connectLock) {
                    closeCrap();
                }
                fireConnectorEvent(new ConnectorEvent(this, ConnectorEvent.DISCONNECTED, null));
            }
            deleteListeners();
        }
    }

    public synchronized void addListener(ConnectorListener listener){
        this.listeners.add(listener);
    }

    public synchronized void removeListener(ConnectorListener listener){
        this.listeners.remove(listener);
    }

    public synchronized void deleteListeners(){
        this.listeners.clear();
    }

    public synchronized void fireConnectorEvent(ConnectorEvent event){
        for(ConnectorListener l : listeners) l.notifyAboutConnectorEvent(event);
    }
}
