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
Kontroler panelu rejestracji, przyjmuje pseudonim użytkownika, oraz dwukrotnie hasło,
wysyła je do serwera, porzez NetworkController
 */

public class RegistrationPanel extends GridPane implements Initializable, LangListener, DataListener {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @FXML
    private Label registrationLabel;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField confimPassword;
    @FXML
    private JFXButton registrationButton;

    private LangBean langBean;

    private JFXSpinner spinner = new JFXSpinner();
    private boolean isDataSend = false;

    private ConfigManager config = null;
    private LangController lang = null;
    private NetworkController net;

    public RegistrationPanel(ConfigManager config){
        this.config = config;
        this.lang = LangController.getInstance();
        net = NetworkController.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getClass().getResource("/view/registrationWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            LOGGER.info("RegistrationPanel - załadowano widok");
        } catch (IOException exception) {
            LOGGER.severe("RegistrationPanel - nie można załadować widoku");
            throw new FailedToLoadViewException(this);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lang.addLanguageListener(this);
        lang.loadDefaultLang();
        net.addDataListener(this);
        spinner.setRadius(8);
        registrationLabel.getStyleClass().add("login-text");
        username.getStyleClass().add("login-text");
        password.getStyleClass().add("login-text");
        confimPassword.getStyleClass().add("login-text");
        registrationButton.getStyleClass().add("login-button");
        this.getStyleClass().add("login-back");
        registrationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!username.getText().isEmpty() && !password.getText().isEmpty() && isPasswordMatch() && !isDataSend){
                    isDataSend = true;
                    registrationButton.setText("");
                    registrationButton.setGraphic(spinner);
                    List<String> credentials = new ArrayList<String>();
                    credentials.add(username.getText());
                    credentials.add(password.getText());
                    DataProtocol data = new DataProtocol(DataProtocol.REG_NEW_USER, credentials);

                    try{
                        net.sendData(data);
                        LOGGER.info("RegistrationPanel - wysłano żądanie rejestracji");
                    }catch (UnableToSendException e){
                        isDataSend = false;
                        registrationButton.setText(langBean.getRegistrationButton());
                        registrationButton.setGraphic(null);
                        registrationLabel.setText(langBean.getUnableToSend());
                        LOGGER.warning("RegistrationPanel - nie można wysłać żądania rejestracji");
                    }
                }
            }
        });
    }

    private boolean isPasswordMatch(){
        if(!password.getText().equals(confimPassword.getText())){
            registrationLabel.setText(langBean.getRegistrationLabelPassError());
            return false;
        }
        else{
            registrationLabel.setText(langBean.getRegistrationLabel());
            return true;
        }
    }

    @Override
    public void loadLang(LangEvent event) {
        langBean = event.getLangBean();
        username.setPromptText(langBean.getLoginUsername());
        password.setPromptText(langBean.getLoginPassword());
        confimPassword.setPromptText(langBean.getConfimPassword());
        registrationButton.setText(langBean.getRegistrationButton());
        registrationLabel.setText(langBean.getRegistrationLabel());
        LOGGER.info("RegistrationPanel - przeładowano język");
    }

    @Override
    public void onDataRecieved(final DataProtocol data) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                isDataSend = false;
                registrationButton.setText(langBean.getRegistrationButton());
                registrationButton.setGraphic(null);
                switch (data.getMetadata()){
                    case DataProtocol.REG_USER_EXISTS:
                        registrationLabel.setText(langBean.getRegistrationLabelUserExsistsError());
                        LOGGER.info("RegistrationPanel - odebrano 'użytkownik już istnieje w bazie'");
                        break;
                    case DataProtocol.REG_SUCCESS:
                        registrationLabel.setText(langBean.getRegistrationLabelSuccess());
                        LOGGER.info("RegistrationPanel - zarejestrowano");
                        break;
                }
            }
        });

    }
}
