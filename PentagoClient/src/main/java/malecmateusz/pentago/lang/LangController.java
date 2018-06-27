package malecmateusz.pentago.lang;

import javafx.concurrent.Worker;
import malecmateusz.pentago.config.ConfigManager;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/*
kontroler modułu językowego, wywoływany przy wybraniu przez uzytkownika zmiany języka,
łapie on wynik wykonania wątku parsującego plik xml, i gdy wątek zakończy działanie, notyfikuje on
obserwatorów zmiany języka
 */
public class LangController implements EventHandler<WorkerStateEvent> {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private List<LangListener> langListeners = new ArrayList<LangListener>();
    private static LangController instance;
    private LangLoader loader;
    private LangBean activeBean;
    private Object beanLock = new Object();
    ConfigManager config;

    private LangController(){
        loader = new LangLoader();
        langListeners = new ArrayList<LangListener>();
        loader.setOnSucceeded(this);
        loader.setOnFailed(this);
        try {
            config = ConfigManager.getInstance();
        } catch (IOException e) {
            LOGGER.severe("Nie udało się pobrać instancji ConfigManager'a");
        }
    }

    public static synchronized LangController getInstance() {
        if(instance == null) instance = new LangController();
        return instance;
    }

    public void loadDefaultLang(){
        loadLang((String)config.getValue("LANG"));
    }

    public void loadLang(String acronim){
        loader.setAcronim(acronim);
        loader.restart();
    }
    @Override
    public void handle(WorkerStateEvent event) {
        if(event.getSource().getState() == Worker.State.SUCCEEDED){
            LangBean bean = (LangBean)event.getSource().getValue();
            fireLanguageEvent(bean);
            synchronized (beanLock){
                activeBean = bean;
            }
            LOGGER.info("Worker załadował język");
        } else if(event.getSource().getState() == Worker.State.FAILED){
            LOGGER.severe("Nie odnaleziono plików języka");
            System.exit(0);
        }
    }

    public LangBean getActiveBean() {
        LangBean bean;
        synchronized (beanLock){
            bean = activeBean;
        }
        return bean;
    }

    public synchronized void addLanguageListener(LangListener l){
        langListeners.add(l);
    }
    public synchronized void removeLanguageListener(LangListener l){
        langListeners.remove(l);
    }

    private synchronized void fireLanguageEvent(LangBean bean){
        LangEvent event = new LangEvent(this, bean);
        for(LangListener l : langListeners) l.loadLang(event);
    }
}
