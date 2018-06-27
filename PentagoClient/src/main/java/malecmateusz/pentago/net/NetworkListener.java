package malecmateusz.pentago.net;
/*
słuchacz stanu połaczenia sieciowego, są nimi kontrolery komponentów GUI
 */
public interface NetworkListener {
    void onConnected();
    void onConnectionFailure();
    void onDosconnected();

}
