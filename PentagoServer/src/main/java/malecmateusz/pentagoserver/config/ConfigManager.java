package malecmateusz.pentagoserver.config;

import java.io.*;
import java.util.Properties;

public class ConfigManager {

    private static String CONFIG_FILENAME = "config.properties";
    private static ConfigManager instance;

    private Properties config;
    private InputStream input = null;
    private OutputStream output = null;

    private ConfigManager() throws IOException {
        loadConfig();
    }

    private void loadConfig() throws IOException {
        config = new Properties();
        input = new FileInputStream(System.getProperty("user.home") + "/JavaFXServer/" + CONFIG_FILENAME);

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
        output = new FileOutputStream(System.getProperty("user.home") + "/JavaFXServer/" + CONFIG_FILENAME);
        System.out.println(System.getProperty("user.home") + "/JavaFXServer/" + CONFIG_FILENAME);
        config.store(output,"");
        output.close();

    }
}
