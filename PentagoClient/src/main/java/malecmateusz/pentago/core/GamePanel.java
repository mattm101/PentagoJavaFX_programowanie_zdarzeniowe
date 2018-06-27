package malecmateusz.pentago.core;

import javafx.scene.control.ScrollPane;
import malecmateusz.pentago.components.notyfication.Information;
import malecmateusz.pentago.components.notyfication.Notyfication;
import malecmateusz.pentago.config.ConfigManager;
import malecmateusz.pentago.lang.LangBean;
import malecmateusz.pentago.lang.LangEvent;
import malecmateusz.pentago.lang.LangListener;
import malecmateusz.pentago.net.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/*
Kotroler panelu gry w głównym oknie aplikacji, zarządza wyświetlaniem czatu, oraz wyśiwtlaniem
zaproszeń wysłanych przez innych użytkowników
 */

public class GamePanel extends StackPane implements Initializable, DataListener, LangListener{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private ConfigManager config;
    private BorderPane borderPane;
    private TextArea chat;
    private NetworkController net;
    private GameWindow gameWindow;
    private LangBean langBean;
    private String you;
    private TextField messageField;
    private VBox notyficationsBox;
    private ScrollPane notyficationsScroll;
    private Object notyficationsLock = new Object();
    public GamePanel(ConfigManager config) {
        this.config = config;
        chat = new TextArea();
        messageField = new TextField();
        borderPane = new BorderPane();
        notyficationsBox = new VBox();
        net = NetworkController.getInstance();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/GamePanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new FailedToLoadViewException(this);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chat.setMinSize(400, 300);
        messageField.setMinSize(400, 50);
        notyficationsScroll = new ScrollPane();
        notyficationsBox.setMaxWidth(200);
        notyficationsBox.setSpacing(10);
        notyficationsScroll.setMaxWidth(240);
        notyficationsScroll.setContent(notyficationsBox);
        notyficationsScroll.getStyleClass().add("noty-scroll");
        notyficationsBox.getStyleClass().add("noty-scroll");
        this.getChildren().addAll(borderPane, notyficationsScroll);
        StackPane.setAlignment(notyficationsScroll, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(borderPane, Pos.TOP_LEFT);
        StackPane.setMargin(notyficationsScroll, new Insets(0, 20, 50, 0));
        borderPane.setCenter(chat);
        borderPane.setBottom(messageField);
        messageField.setPromptText("Wiadomość...");
        messageField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!messageField.getText().isEmpty()) {
                    try {
                        net.sendData(new DataProtocol(DataProtocol.CHAT_BROADCAST, messageField.getText()));
                        chat.appendText(you +": " + messageField.getText() + "\n");
                        LOGGER.info("Wysłano wiadomość na chacie");
                    }catch(UnableToSendException e){
                        chat.appendText(you +": " + messageField.getText() + "(OFFLINE)\n");
                        LOGGER.warning("Nie można wysłać wiadomości");
                    }
                    messageField.clear();

                }
            }
        });
        chat.setWrapText(true);
        chat.setEditable(false);
        net.addDataListener(this);
        chat.getStyleClass().add("chat-box");
        messageField.getStyleClass().add("message-field");
    }

    @Override
    public void onDataRecieved(final DataProtocol data) {
        switch(data.getMetadata()){
            case DataProtocol.CHAT_BROADCAST:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chat.appendText((String)data.getData() + "\n");
                    }
                });
                break;
            case DataProtocol.REQUEST_CHALLANGE:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        Notyfication notyfication = new Notyfication((String) data.getData(), notyficationsBox);
                        if(!Notyfication.contains(notyfication)){
                            Notyfication.add(notyfication);
                            notyficationsBox.getChildren().add(notyfication);
                        }




                    }
                });
                break;
            case DataProtocol.USER_LEAVED:

                Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Notyfication notyToRemove = Notyfication.findByNick((String) data.getData());
                            if(notyToRemove != null){
                                Notyfication.remove(notyToRemove);
                                notyficationsBox.getChildren().remove(notyToRemove);
                            }
                        }
                    });
                break;
            case DataProtocol.CANNOT_START_A_GAME:
                LOGGER.info("odebrano 'nie można stworzyć gry'");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Information info = new Information((String) data.getData(), notyficationsBox);
                        notyficationsBox.getChildren().add(info);
                    }
                });
                break;
            case DataProtocol.START_GAME:
                LOGGER.info("odebrano 'nowa gra'");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameWindow = new GameWindow(false, Window.nickname);
                    }
                });
                break;
            case DataProtocol.START_GAME_FIRST:
                LOGGER.info("odebrano 'nowa gra - ty zaczynasz'");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameWindow = new GameWindow( true, Window.nickname);
                    }
                });
                break;
        }
    }

    @Override
    public void loadLang(LangEvent event) {
        this.langBean = event.getLangBean();
        messageField.setPromptText(langBean.getMsgPrompt());
        you = langBean.getMsgYou();
        LOGGER.info("przeładowano język");
    }
}
