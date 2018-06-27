package malecmateusz.pentago.core;

/*
Wyjątek wyrzucany gdy nie udało się załadować widoku z pliku FXML
 */

public class FailedToLoadViewException extends RuntimeException {
    private Object source;
    public FailedToLoadViewException(Object source){
        super();
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
