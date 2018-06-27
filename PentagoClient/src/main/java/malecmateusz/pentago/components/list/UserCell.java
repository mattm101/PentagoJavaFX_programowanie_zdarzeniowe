package malecmateusz.pentago.components.list;

import com.jfoenix.controls.JFXSpinner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
/*
komórka listy użytkowników,
zawera przycisk pozwalający na wyzwanie innego gracza na pojedynek
 */
public class UserCell extends HBox {
    private final String user;
    private boolean requestSend = false;
    private boolean isFriend = false;
    private Button fightButton;
    private Label usernameLabel;
    private final UserList parent;
    private JFXSpinner spinner = new JFXSpinner();
    private Image image;

    public UserCell(final UserList parent, final String user){
        image = new Image("image/fight.png", 20,20, false, true);
        Pane spacer=new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10,1);
        this.setMinWidth(200);
        this.user = user;
        usernameLabel = new Label(user);
        usernameLabel.setPadding(new Insets(5,5,5,5));
        usernameLabel.getStyleClass().add("user-cell-label");
        this.setPadding(new Insets(5,5,5,5));

        this.parent = parent;
        fightButton = new Button();
       // fightButton.setPadding(new Insets(5,5,5,5));
        fightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!requestSend) {
                    //requestSend = true;
                    parent.sendFightRequest(user);
                    fightButton.setText("");
                    fightButton.setGraphic(spinner);
                }
            }
        });
        this.getStyleClass().add("user-cell");
        this.getChildren().addAll(usernameLabel,spacer,fightButton);
        spinner.setRadius(6);
        fightButton.setMaxSize(40 ,40);
        fightButton.setGraphic(new ImageView(image));
        fightButton.getStyleClass().add("user-cell-button");
    }

    public void fightCancelled(){
        fightButton.setGraphic(new ImageView(image));
    }

    public void fightAccepted(){
        fightButton.setGraphic(new ImageView(image));
    }

    public String getUser(){
        return user;
    }
}
