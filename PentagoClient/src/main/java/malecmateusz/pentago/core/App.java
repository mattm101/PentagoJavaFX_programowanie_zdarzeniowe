package malecmateusz.pentago.core;

import javafx.scene.image.Image;
import malecmateusz.pentago.config.ConfigManager;
import malecmateusz.pentago.util.PentagoLogger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import malecmateusz.pentago.util.ThemeManager;

import java.io.IOException;
import java.util.logging.Logger;


/**
 *
 *
 */
public class App extends Application{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    Stage window;
    Scene scene;
    StartWindow content;
    private Image icon;
    public static void main( String[] args ) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        try{
            PentagoLogger.setup();
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException("Logger nie wystartował");
        }
        ConfigManager config = null;
        try{
            config = ConfigManager.getInstance();
        }catch (IOException e){
            LOGGER.severe("Nie znaleziono pliku konfiguracyjnego");
            System.exit(0);
        }
        icon = new Image("image/icon.png");
        content = new StartWindow(config);
        window = primaryStage;
        window.setMinHeight(460);
        window.setMaxHeight(460);
        window.setMaxWidth(350);
        window.setMinWidth(350);
        window.setResizable(false);
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                window.close();
                System.exit(0);
            }
        });

        scene = new Scene(content);
        window.setTitle("Pentago");
        ThemeManager.getInstance().setScene(scene);
        ThemeManager.getInstance().loadDefaultTheme();
        window.getIcons().addAll(icon);
        window.setScene(scene);
        window.show();

    }
}
