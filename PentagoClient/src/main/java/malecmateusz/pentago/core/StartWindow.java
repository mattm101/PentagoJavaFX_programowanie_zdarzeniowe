package malecmateusz.pentago.core;

import com.jfoenix.controls.*;
import javafx.scene.image.Image;
import malecmateusz.pentago.config.ConfigManager;
import malecmateusz.pentago.lang.LangBean;
import malecmateusz.pentago.lang.LangController;
import malecmateusz.pentago.lang.LangEvent;
import malecmateusz.pentago.lang.LangListener;
import malecmateusz.pentago.net.DataListener;
import malecmateusz.pentago.net.DataProtocol;
import malecmateusz.pentago.net.NetworkController;
import malecmateusz.pentago.net.NetworkListener;
import malecmateusz.pentago.util.ThemeManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/*
Kontroleg okna startowego, wywołuje on panele logowania i rejestracji, i zarządza nimi,
po pomyślnym zalogowaniu uruchamia on także główne okno aplikacji
 */

public class StartWindow extends BorderPane implements Initializable, LangListener, NetworkListener, DataListener {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @FXML
    private JFXTabPane startPane;
    @FXML
    private StackPane stack;

    private String nickname;
    private LoginPanel loginPanel = null;
    private RegistrationPanel registrationPanel = null;
    private Tab loginTab = null;
    private Tab registrationTab = null;
    private Rectangle loadingRectangle = new Rectangle(350,450);
    private Group loadingGroup = new Group();
    private JFXSpinner loadingSpinner = new JFXSpinner();
    private Label errorLabel;


    private ConfigManager config = null;
    private LangController lang = null;
    private NetworkController net = null;

    public StartWindow(ConfigManager config){
        this.config = config;
        this.lang = LangController.getInstance();
        this.net = NetworkController.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getClass().getResource("/view/startWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            LOGGER.info("Załadowano widok startowego okna");
        } catch (IOException exception) {
            LOGGER.severe("Nie uadało się załadować widoku okna startowego");
            throw new FailedToLoadViewException(this);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginPanel = new LoginPanel(config);
        registrationPanel = new RegistrationPanel(config);
        loginTab = new Tab();
        registrationTab = new Tab();
        errorLabel = new Label();
        loginTab.setContent(loginPanel);
        registrationTab.setContent(registrationPanel);
        startPane.getTabs().addAll(loginTab, registrationTab);
        loadingRectangle.setOpacity(0.8);

        lang.addLanguageListener(this);
        lang.loadDefaultLang();
        net.addNetworkListener(this);
        net.addDataListener(this);
        this.getStyleClass().add("login-back");
       showLoadingScreen();
        net.connect();
    }

    private void showLoadingScreen(){
        loadingGroup.getChildren().add(loadingSpinner);
        stack.getChildren().addAll(loadingRectangle, loadingGroup);
    }

    private void hideLoadingScreen(){
        stack.getChildren().remove(loadingRectangle);
        stack.getChildren().remove(loadingGroup);
    }

    private void showConnectionFailureOnLoadingScreen(){
        errorLabel.setStyle("-fx-text-fill: #FFFFFF");
        loadingGroup.getChildren().remove(loadingSpinner);
        loadingGroup.getChildren().add(errorLabel);
    }

    @Override
    public void loadLang(LangEvent event) {
        LangBean langBean = event.getLangBean();
        loginTab.setText(langBean.getLoginTabName());
        registrationTab.setText(langBean.getRegistrationTabName());
        errorLabel.setText(langBean.getConnectionErrorMessage());
    }

    private void loadMainWindow(){
        try {
            ConfigManager config = ConfigManager.getInstance();
            final Stage stage = new Stage(StageStyle.DECORATED);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    stage.close();
                    net.disconnect();
                    System.exit(0);
                }
            });
            stage.setTitle("Pentago - " + nickname);
            stage.getIcons().add(new Image("image/icon.png"));
            Scene scene = new Scene(new Window(config, nickname));
            ThemeManager.getInstance().setScene(scene);
            ThemeManager.getInstance().loadDefaultTheme();
            //scene.getStylesheets().add("css/default.css");
            stage.setScene(scene);
            stage.show();
            LOGGER.info("StartWindow - Przygotowano ładowanie głównego okna");
        } catch (IOException e) {
            LOGGER.severe("StartWindow - Wyjątek podczas przygotowywania głównego okna");
            System.exit(0);
        }
    }

    private void closeWindow(){
        ((Stage)this.getScene().getWindow()).close();
    }

    @Override
    public void onConnected() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                hideLoadingScreen();
            }
        });
    }

    @Override
    public void onConnectionFailure() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showConnectionFailureOnLoadingScreen();
            }
        });

    }

    @Override
    public void onDosconnected() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showConnectionFailureOnLoadingScreen();
            }
        });
    }

    @Override
    public void onDataRecieved(final DataProtocol data) {
        switch (data.getMetadata()){
            case DataProtocol.LOGIN_SUCCESS:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        nickname = (String) data.getData();
                        closeWindow();
                        loadMainWindow();
                    }
                });
                break;
        }
    }
}
