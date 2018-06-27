package malecmateusz.pentago.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import malecmateusz.pentago.REST.TimeListener;
import malecmateusz.pentago.REST.TimeTimer;
import malecmateusz.pentago.components.popupmenu.PopMenu;
import malecmateusz.pentago.components.list.UserList;
import malecmateusz.pentago.config.ConfigManager;
import malecmateusz.pentago.lang.LangBean;
import malecmateusz.pentago.lang.LangController;
import malecmateusz.pentago.lang.LangEvent;
import malecmateusz.pentago.lang.LangListener;
import malecmateusz.pentago.util.ThemeManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/*
Kontroler głównego okna aplikacji, ładuje widok głównego okna z pliku FXML,
tworzy głowne menu, oraz wywołuje panele gry, oraz statystyk, inicjalizuje także menu ustawień

 */

public class Window extends BorderPane implements EventHandler<ActionEvent>, Initializable, LangListener, TimeListener{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @FXML
    StackPane centerPanel;
    GamePanel gamePanel;
    AccountPanel accountPanel;
    @FXML
    Button gameButton;
    @FXML
    Button accountButton;
    @FXML
    Button optionsButton;
    @FXML
    HBox menuBox;
    @FXML
    Pane spacer;
    @FXML
    Pane spacerRight;
    @FXML
    Pane blurPane;
    @FXML
    Label timeLabel;

    private List<MainMenuListener> listeners = new ArrayList<MainMenuListener>();
    public static String nickname;
    private PopMenu popupMenu;
    private UserList userList;
    private JFXButton popPLBtn;
    private JFXButton popENBtn;
    private TimeTimer time;
    private boolean optionsOn = false;
    private ConfigManager config = null;
    private LangController lang = null;
    private List<Button> buttons = new ArrayList<Button>();
    public Window(ConfigManager config, String nickname) {
        this.config = config;
        this.nickname = nickname;
        this.lang = LangController.getInstance();
        this.gamePanel = new GamePanel(config);
        this.accountPanel = new AccountPanel(config);
        this.time = TimeTimer.getInstance();
        this.addListener(accountPanel);
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getClass().getResource("/view/Window.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            LOGGER.info("Załadowano widok głównego okna");
        } catch (IOException exception) {
            LOGGER.severe("Nie udało się załadować widoku głównego okna");
            throw new FailedToLoadViewException(this);
        }
        this.setPrefSize(Double.valueOf((String) config.getValue("SCREEN_WIDTH")), Double.valueOf((String)config.getValue("SCREEN_HEIGHT")));
        BorderPane.setMargin(centerPanel, new Insets(0,0,0,0));
       BorderPane.setAlignment(centerPanel, Pos.CENTER);
    }

    public void handle(ActionEvent event) {
        Button b = (Button)event.getSource();

            if(b == gameButton) {

                centerPanel.getChildren().clear();
                centerPanel.getChildren().add(gamePanel);
                setActive(b);
                Effect.fadeIn(gamePanel);
                this.setLeft(userList);
                Effect.fadeIn(userList);
            }else if(b == accountButton) {
                this.setLeft(null);
                centerPanel.getChildren().clear();
                centerPanel.getChildren().add(accountPanel);
                setActive(b);

                Effect.fadeIn(accountPanel);
                fireMainMenuEvent();
            }else if(b == optionsButton) {
                if(!optionsOn) {
                    optionsOn = true;
                    popupMenu.showBelow(b, this);

                    optionsButton.getStyleClass().add("main-button-active");
                    optionsButton.setEffect(new Glow(1.0));

                }else{
                   hidePopup();
                }
            }

    }


    public void setActive(Button activeButton){
        for(Button b : buttons){
            b.getStyleClass().removeAll("main-button-active");
            b.getStyleClass().add("main-button");

            b.setEffect(null);
        }
        activeButton.getStyleClass().add("main-button-active");
        activeButton.setEffect(new Glow(1.0));
    }

    public void initialize(URL location, ResourceBundle resources) {
        //this.setPrefSize(Double.valueOf((String)config.getValue("SCREEN_WIDTH")), Double.valueOf((String)config.getValue("SCREEN_HEIGHT")));
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        HBox.setHgrow(blurPane, Priority.ALWAYS);
        HBox.setHgrow(blurPane, Priority.ALWAYS);
        BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(3);
        blurPane.getStyleClass().add("blur-box");
       blurPane.setEffect(bb);
        spacerRight.setMaxWidth(20);
        buttons.add(gameButton);
        buttons.add(accountButton);
        buttons.add(optionsButton);
        for(Button b : buttons) b.setOnAction(this);
        setActive(gameButton);
        userList = new UserList(200, Double.valueOf((String)config.getValue("SCREEN_HEIGHT")) - 40.0);
        centerPanel.getChildren().add(gamePanel);
        this.setLeft(userList);
        menuBox.getStyleClass().add("menu-box");
        timeLabel.setStyle("-fx-background-color: none; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 12");
        lang.addLanguageListener(this);
        lang.addLanguageListener(gamePanel);
        lang.addLanguageListener(accountPanel);
        lang.loadDefaultLang();
        time.addListener(this);
    }

    private void hidePopup(){
        popupMenu.hide();
        optionsOn = false;
        optionsButton.getStyleClass().removeAll("main-button-active");
        optionsButton.getStyleClass().add("main-button");
        optionsButton.setEffect(null);
    }

    private void initPopMenu(LangBean langBean){
        popPLBtn = new JFXButton(langBean.getPopPLBtn());
        popENBtn = new JFXButton(langBean.getPopENBtn());
        JFXButton popTH1Btn =  new JFXButton("Deepblue");
        JFXButton popTH2Btn =  new JFXButton("Inferno");
        JFXButton popTH3Btn =  new JFXButton("Illidan");
        Slider slider = new JFXSlider();
        if(popupMenu != null){
            hidePopup();
        }
        popPLBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LangController.getInstance().loadLang("PL");
                config.setValue("LANG", "PL");
                try {
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        popENBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LangController.getInstance().loadLang("EN");
                config.setValue("LANG", "EN");
                try {
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        popTH1Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ThemeManager.getInstance().loadTheme(1);
            }
        });
        popTH2Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ThemeManager.getInstance().loadTheme(2);
            }
        });
        popTH3Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ThemeManager.getInstance().loadTheme(3);
            }
        });
        popupMenu = new PopMenu();
        popupMenu.addLabel(langBean.getPopLangLbl()).addElement(popPLBtn).addElement(popENBtn).addSeparator()
                .addLabel(langBean.getPopVolLbl()).addElement(popTH1Btn).addElement(popTH2Btn).addElement(popTH3Btn).build();
        popupMenu.setOnAutoHide(new EventHandler<Event>() {
            public void handle(Event e) {
                hidePopup();
            }
        });
    }

    @Override
    public void loadLang(LangEvent event) {
        LangBean langBean = event.getLangBean();
        initPopMenu(langBean);
        gameButton.setText(langBean.getMainMenuGameButton());
        accountButton.setText(langBean.getMainMenuAccountButton());
    }


    public synchronized void addListener(MainMenuListener l){
        listeners.add(l);
    }
    public synchronized void removeListener(MainMenuListener l){
        listeners.remove(l);
    }

    private synchronized void fireMainMenuEvent(){
        for(MainMenuListener l : listeners) l.onClick();
    }

    @Override
    public void timeArrived(final String time) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(!time.equals("--:--")) timeLabel.setText(time.substring(0, time.length() - 3));
                else timeLabel.setText(time);
            }
        });
    }
}
