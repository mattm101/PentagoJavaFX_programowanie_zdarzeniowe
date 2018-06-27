package malecmateusz.pentago.lang;
import java.util.EventObject;

public class LangEvent extends EventObject {
    private LangBean langBean;
    public LangEvent(Object source, LangBean langBean) {
        super(source);
        this.langBean = langBean;
    }

    public LangBean getLangBean() {
        return langBean;
    }
}