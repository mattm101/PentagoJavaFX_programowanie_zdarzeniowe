package malecmateusz.pentago.lang;

import com.thoughtworks.xstream.XStream;
import javafx.concurrent.Task;

import java.io.File;
import java.util.logging.Logger;
/*
klasa parsująca plik xml z językiem w nowym wątku
 */
public class LangLoaderTask extends Task<LangBean> {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String path = null;
    public LangLoaderTask(String path){
        this.path = path;
    }
    protected LangBean call() throws Exception {
        XStream parser = new XStream();
        XStream.setupDefaultSecurity(parser);
        Class<?>[] classes = new Class[]{LangBean.class};
        parser.allowTypes(classes);
        parser.processAnnotations(LangBean.class);
        Object readObject = parser.fromXML(new File(path));
        LOGGER.info("LangLoaderTask - wczytano język z pliku XML");
        return (LangBean) readObject;
    }
}
