package malecmateusz.pentago.lang;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
/*
klasa modelu w module języka, uruchamia wątek parsujący plik xml z językiem
 */
public class LangLoader extends Service<LangBean> {
    private String acronim = null;
    private final String pathEN = System.getProperty("user.home") + "/JavaFXTest/locale/lang_EN.xml";
    private final String pathPL = System.getProperty("user.home") + "/JavaFXTest/locale/lang_PL.xml";
    private String path = null;
    protected Task<LangBean> createTask() {

        return new LangLoaderTask(path);
    }

    public void setAcronim(String acronim) {

        this.acronim = acronim;
        if(acronim.equals("PL")) path = pathPL;
        else if(acronim.equals("EN")) path = pathEN;

    }
}
