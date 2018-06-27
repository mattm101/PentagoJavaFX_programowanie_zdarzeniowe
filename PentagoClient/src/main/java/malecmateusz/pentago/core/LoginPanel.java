package malecmateusz.pentago.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import malecmateusz.pentago.config.ConfigManager;
import malecmateusz.pentago.lang.LangBean;
import malecmateusz.pentago.lang.LangController;
import malecmateusz.pentago.lang.LangEvent;
import malecmateusz.pentago.lang.LangListener;
import malecmateusz.pentago.net.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/*
Kontroler panelu logowania, pozwala wprowadzić użytkownikowi login i hasło w celu zalogowania
 */

public class LoginPanel extends GridPane implements Initializable, LangListener, DataListener {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton loginButton;
    @FXML
    private Label loginLabel;

    private JFXSpinner loginSpinner = new JFXSpinner();

    private ConfigManager config = null;
    private LangController lang = null;
    private NetworkController net = null;
    private boolean isDataSend = false;
    private LangBean langBean;

    public LoginPanel(ConfigManager config){
        this.config = config;
        this.lang = LangController.getInstance();
        this.net = NetworkController.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getClass().getResource("/view/loginWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            LOGGER.info("Załadowano widok");
        } catch (IOException exception) {
            LOGGER.severe("Nie udało się załadować widoku");
            throw new FailedToLoadViewException(this);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lang.addLanguageListener(this);
        lang.loadDefaultLang();
        net.addDataListener(this);
        loginLabel.getStyleClass().add("login-text");
        loginSpinner.setRadius(8);
        username.getStyleClass().add("login-text");
        password.getStyleClass().add("login-text");
        this.getStyleClass().add("login-back");
        loginLabel.setStyle("-fx-text-fill: #FFFFFF");
        loginButton.getStyleClass().add("login-button");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isDataSend == false) {

                    List<String> credentials = new ArrayList<String>();
                    credentials.add(username.getText());
                    credentials.add(password.getText());
                    try {
                        net.sendData(new DataProtocol(DataProtocol.LOGIN, credentials));
                        isDataSend = true;
                        loginButton.setGraphic(loginSpinner);
                        loginButton.setText("");
                    }catch(UnableToSendException e){
                        LOGGER.warning("Nie mogę wysłać danych logowania do serwera");
                    }

                }

            }
        });
    }

    @Override
    public void loadLang(LangEvent event) {
        langBean = event.getLangBean();
        username.setPromptText(langBean.getLoginUsername());
        password.setPromptText(langBean.getLoginPassword());
        loginButton.setText(langBean.getLoginButton());
        loginLabel.setText(langBean.getLoginLabel());
        LOGGER.info("Przeładowano język");
    }



    @Override
    public void onDataRecieved(DataProtocol data) {
        switch(data.getMetadata()){
            case DataProtocol.LOGIN_FAILURE:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loginButton.setGraphic(null);
                        loginButton.setText(langBean.getLoginButton());
                        loginLabel.setText(langBean.getLoginLabelError());
                        isDataSend = false;
                        LOGGER.info("Nie udało się zalogować");

                    }
                });
                break;


        }
    }
}
