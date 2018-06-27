package malecmateusz.pentago.net;
/*
interfejs obserwatora odebrania danych z serwera, słuchaczami są elementy GUI
 */
public interface DataListener {
    void onDataRecieved(DataProtocol data);
}
