package malecmateusz.pentago.components.notyfication;

import malecmateusz.pentago.lang.LangController;
import malecmateusz.pentago.lang.LangEvent;
import malecmateusz.pentago.lang.LangListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
/*
okienko informacyjne, pojawia się gdy użytkownik, którego zapraszamy do gry, już jest w grze
 */
public class Information extends HBox implements LangListener{
    private String nickname;
    private Label label;
    private Button closeButton;
    private LangController lang = LangController.getInstance();
    private VBox parent;

    public Information(final String nickname, VBox parent){
        this.nickname = nickname;
        this.parent = parent;
        lang.addLanguageListener(this);
        label = new Label(nickname + lang.getActiveBean().getInformation());
        this.setMinWidth(200);
        this.setPadding(new Insets(5,5,5,5));
        this.setSpacing(10);
        label.setMinSize(100,30);

        Pane spacer=new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10,1);
        closeButton = new Button("close");

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               delete();
            }
        });

        this.getChildren().addAll(label, spacer, closeButton);
    }

    public String getNickname(){
        return nickname;
    }

    @Override
    public void loadLang(LangEvent event) {
        label.setText(nickname + event.getLangBean().getInformation());
    }

    public void delete(){
        lang.removeLanguageListener(this);
        parent.getChildren().remove(this);
    }
}
