package malecmateusz.pentago.net;
/*
wyjątek wyrzucany gdy nie można wysłać wiadomości do serwera
 */
public class UnableToSendException extends RuntimeException{
    private DataProtocol data;
    public UnableToSendException(DataProtocol data){
        super();
        this.data = data;
    }

    public DataProtocol getData() {
        return data;
    }
}
