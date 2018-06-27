package malecmateusz.pentago.util;

import malecmateusz.pentago.config.ConfigManager;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Logger;
/*
klasa ładująca wybrany css ze skórką aplikacji
 */
public class ThemeManager {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final static String PATH = "css/";
    private final static String THEME_1 = "default";
    private final static String THEME_2 = "default2";
    private final static String THEME_3 = "default3";
    private static ThemeManager instance;

    private Scene scene;
    private ConfigManager config;

    private ThemeManager(){
        try {
            config = ConfigManager.getInstance();
            LOGGER.info("Załadowano plik config.properties");
        } catch (IOException e) {
            LOGGER.severe("Nie udało się załadować pliku config.properties");
        }
    }

    public static ThemeManager getInstance(){
        if(instance == null) instance = new ThemeManager();
        return instance;
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void loadDefaultTheme(){
        if(scene != null){
            scene.getStylesheets().add(PATH + (String)config.getValue("THEME") + ".css");
        }
    }

    public void loadTheme(int nr){
        if(scene != null){
            scene.getStylesheets().remove(PATH + THEME_1 + ".css");
            scene.getStylesheets().remove(PATH + THEME_2 + ".css");
            scene.getStylesheets().remove(PATH + THEME_3 + ".css");
            String value = (String)config.getValue("THEME");
            switch (nr){
                case 1:
                    value = THEME_1;
                    scene.getStylesheets().add(PATH + THEME_1 + ".css");
                    break;
                case 2:
                    value = THEME_2;
                    scene.getStylesheets().add(PATH + THEME_2 + ".css");
                    break;
                case 3:
                    value = THEME_3;
                    scene.getStylesheets().add(PATH + THEME_3 + ".css");
                    break;
            }
            config.setValue("THEME", value);
            try {
                config.save();
                LOGGER.info("Zapisano nową konfigurację THEME");
            } catch (IOException e) {
                LOGGER.severe("Nie udało się zapisać nowej konfiguracji THEME");
            }

        }
    }
}
