package malecmateusz.pentago.components.list;

import malecmateusz.pentago.net.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserList extends ScrollPane implements DataListener {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private NetworkController net = NetworkController.getInstance();
    private VBox usersBox = new VBox();
    List<UserCell> users = new ArrayList<UserCell>();
    private Object userLock = new Object();

    public UserList(final double width, final double height){


        this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setContent(usersBox);
        this.setPrefSize(width, height);
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.net.addDataListener(this);
        usersBox.getStyleClass().add("user-list");
        this.getStyleClass().add("user-list-scroll");
        sendListRequest();
    }

    public void setUsers(List<String> usernames){
        synchronized (userLock){
        usersBox.getChildren().removeAll(users);
        users.clear();
        for(String username : usernames){
            UserCell userCell = new UserCell(this, username);
            users.add(userCell);
            usersBox.getChildren().add(userCell);
        }
        }
    }


    public void addUser(String username){
        synchronized (userLock){
        boolean exists = false;
        for(UserCell u : users) if(u.getUser().equals(username)) exists = true;
        if(!exists) {
            UserCell userCell = new UserCell(this, username);
            users.add(userCell);
            usersBox.getChildren().add(userCell);
        }
        }
    }

    public void removeUser(String username){
        synchronized (userLock) {
            if (!users.isEmpty())
                for (UserCell u : users) {
                    if (u.getUser().equals(username)) {
                        users.remove(u);
                        usersBox.getChildren().remove(u);
                    }
                }
        }

    }

    private void fightAccepted(String username){
        synchronized (userLock) {
            if (!users.isEmpty()) {
                for (UserCell u : users) {
                    if (u.getUser().equals(username)) {
                        u.fightAccepted();
                    }
                }
            }
        }
    }

    private void fightCancelled(String username){
        synchronized (userLock) {
            if (!users.isEmpty()) {
                for (UserCell u : users) {
                    if (u.getUser().equals(username)) {
                        u.fightCancelled();
                    }
                }
            }
        }
    }

    public synchronized void sendFightRequest(String username){
        try {
            net.sendData(new DataProtocol(DataProtocol.REQUEST_CHALLANGE, username));
        }catch(UnableToSendException e){
           LOGGER.severe("Nie mogę wysłać wyzwania na pojedynek");
        }
    }

    private synchronized void sendListRequest(){
        try {
            net.sendData(new DataProtocol(DataProtocol.REQUEST_USER_LIST, null));
        }catch(UnableToSendException e){
            LOGGER.severe("Nie mogę wysłać zapytania o listę użytkowników");
        }
    }



    @Override
    public void onDataRecieved(final DataProtocol data) {
        switch (data.getMetadata()){
            case DataProtocol.USER_ENTERED:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        addUser((String)data.getData());
                    }
                });
                break;
            case DataProtocol.RESPONSE_USER_LIST:

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setUsers((List<String>)data.getData());
                    }
                });

                break;
            case DataProtocol.USER_LEAVED:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            removeUser((String) data.getData());
                        }catch (Exception e){
                            LOGGER.severe("Niezrozumiały przez autora wyjątek, który niczego nie psuje " + e.toString());
                        }
                    }
                });
                break;
            case DataProtocol.RESPONSE_CHALLANGE_DECLINED:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fightCancelled((String) data.getData());
                    }
                });
                break;
            case DataProtocol.START_GAME:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fightAccepted((String) data.getData());
                    }
                });
                break;
        }
    }
}
