package malecmateusz.pentago.components.notyfication;

import com.thoughtworks.qdox.model.expression.Not;
import malecmateusz.pentago.lang.LangController;
import malecmateusz.pentago.lang.LangEvent;
import malecmateusz.pentago.lang.LangListener;
import malecmateusz.pentago.net.DataProtocol;
import malecmateusz.pentago.net.NetworkController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
okienko pojawiające się, gdy inny gracz rzuci nam wyzwanie na pojedynek
 */
public class Notyfication extends BorderPane implements LangListener{
    private static List<Notyfication> notyfications = new ArrayList<Notyfication>();

    private String nickname;
    private Label label;
    private Button acceptButton;
    private Button cancelButton;
    private HBox box;
    private LangController lang = LangController.getInstance();
    private NetworkController net = NetworkController.getInstance();
    private VBox parent;
    private Image acc, dec;

    public Notyfication(final String nickname, VBox parent){
        acc =  new Image("image/acc.png", 20,20, false, true);
        dec =  new Image("image/dec.png", 20,20, false, true);
        this.nickname = nickname;
        this.parent = parent;
        lang.addLanguageListener(this);
        label = new Label(nickname + lang.getActiveBean().getNotyfication());
        label.getStyleClass().add("noty-label");
        box = new HBox();
        box.setMinWidth(200);
        box.setPadding(new Insets(5,5,5,5));
        box.setSpacing(10);
        box.getStyleClass().add("noty");
        this.getStyleClass().add("noty");
        this.setPadding(new Insets(5,5,5,10));
        label.setMinSize(200,30);

        acceptButton = new Button();
        cancelButton = new Button();
        acceptButton.setGraphic(new ImageView(acc));
        cancelButton.setGraphic(new ImageView(dec));
        acceptButton.setMaxSize(30,30);
        cancelButton.setMaxSize(30,30);
        acceptButton.getStyleClass().add("noty-button");
        cancelButton.getStyleClass().add("noty-button");

        acceptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                net.sendData(new DataProtocol(DataProtocol.RESPONSE_CHALLANGE_ACCEPTED, nickname));
                delete();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                net.sendData(new DataProtocol(DataProtocol.RESPONSE_CHALLANGE_DECLINED, nickname));
                delete();
            }
        });

        Pane spacerLeft=new Pane();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        Pane spacerRight=new Pane();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        BorderPane.setAlignment(label, Pos.TOP_CENTER);
        BorderPane.setAlignment(box, Pos.CENTER);
        box.getChildren().addAll(spacerLeft,acceptButton, cancelButton, spacerRight);
        this.setTop(label);
        this.setCenter(box);
    }

    public String getNickname(){
        return nickname;
    }

    @Override
    public void loadLang(LangEvent event) {
        label.setText(nickname + event.getLangBean().getNotyfication());
    }

    public void delete(){
        lang.removeLanguageListener(this);
        Notyfication.remove(this);
        parent.getChildren().remove(this);
    }

    public synchronized static void add(Notyfication notyfication){
        notyfications.add(notyfication);
    }

    public synchronized static boolean contains(Notyfication notyfication){
        boolean contains = false;
        for(Notyfication n : notyfications) if(n.getNickname().equals(notyfication.getNickname())) contains = true;
        return contains;
    }

    public synchronized static void remove(Notyfication notyToRemove){
        Iterator<Notyfication> iterator = notyfications.iterator();
        while (iterator.hasNext()){
            Notyfication n = iterator.next();
            if(n.getNickname().equals(notyToRemove.getNickname())) iterator.remove();
        }
    }

    public synchronized static Notyfication findByNick(String nick){
        Notyfication found = null;
        for(Notyfication n : notyfications) if(n.getNickname().equals(nick)) found = n;
        return found;
    }
}
