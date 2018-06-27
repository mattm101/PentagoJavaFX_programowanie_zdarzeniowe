package malecmateusz.pentago.REST;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class Query {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Gson gson;
    public Query(){
        gson = new Gson();
    }
    public String sendTimeRequest() {
        Properties data = null;
        boolean responseStatus = true;
        try {

            URL url = new URL("http://api.timezonedb.com/v2/get-time-zone?key=GUPNQ6D4O7FX&format=json&by=zone&zone=Europe/Warsaw");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                responseStatus = false;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            output = sb.toString();

            data = gson.fromJson(output, Properties.class);
            if(((String)data.getProperty("status")).equals("FAILED")) responseStatus = false;
            conn.disconnect();

        } catch (IOException e) {
            LOGGER.warning("Nie uzyskano poprawnej odpowiedzi serwisu restowego");
            responseStatus = false;
        }

        if(!responseStatus) return "--:--";
        return ((String)data.getProperty("formatted"));

    }
}
