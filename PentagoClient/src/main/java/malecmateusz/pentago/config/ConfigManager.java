package malecmateusz.pentago.config;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;
/*
klasa z przykładu, z laboratorium
wczytuje plik properties i pozwala na jego modyfikację, oraz odczytywanie jego pól
 */
public class ConfigManager {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String CONFIG_FILENAME = "config.properties";
    private static ConfigManager instance;

    private Properties config;
    private InputStream input = null;
    private OutputStream output = null;

    private ConfigManager() throws IOException {
        loadConfig();
        LOGGER.info("Załadowano plik config.properties");
    }

    private void loadConfig() throws IOException {
        config = new Properties();
        input = new FileInputStream(System.getProperty("user.home") + "/JavaFXTest/" + CONFIG_FILENAME);
        LOGGER.info("utworzono ścieżkę do pliku config.properties");
        config.load(input);
        input.close();
    }

    public static synchronized ConfigManager getInstance() throws IOException{
        if(instance == null){
            instance = new ConfigManager();
        }
        return instance;
    }


    public Object getValue(String key){
        return  config.get(key);
    }
    public void setValue(String key,String value){
        config.setProperty(key,value);
    }

    public void save() throws IOException {
        output = new FileOutputStream(System.getProperty("user.home") + "/JavaFXTest/" + CONFIG_FILENAME);
        System.out.println(System.getProperty("user.home") + "/JavaFXTest/" + CONFIG_FILENAME);
        config.store(output,"");
        output.close();
        LOGGER.info("zapisano nową konfigurację w pliku config.properties");
    }
}
