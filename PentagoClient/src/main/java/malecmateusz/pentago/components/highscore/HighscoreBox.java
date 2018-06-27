package malecmateusz.pentago.components.highscore;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
/*
komponent tworzący listę 10 najlepszych graczy
 */
public class HighscoreBox extends ScrollPane {
    private int cells = 0;
    private VBox box;
    private Label titleLabel;
    private HBox titlePanel;

    public HighscoreBox(){
        titlePanel = new HBox();
        titlePanel.setPadding(new Insets(5,5,5,5));
        Pane spacerLeft=new Pane();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        Pane spacerRight=new Pane();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        titleLabel = new Label("Highscores");
        titlePanel.getChildren().addAll(spacerLeft, titleLabel,spacerRight);
        this.box = new VBox();
        this.setMinWidth(200);
        this.setMaxWidth(200);
        this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        box.setPadding(new Insets(0,0,0,0));
        box.getChildren().add(titlePanel);
        this.setContent(box);
        this.getStyleClass().add("high");
        box.getStyleClass().add("high");
        titleLabel.getStyleClass().add("high-title-label");
        titlePanel.getStyleClass().add("high-title-cell");
    }

    public void add(String nickname, int score){
        HighscoreCell tmpCell = new HighscoreCell(++cells, nickname, score);
        box.getChildren().add(tmpCell);
    }

    public void clear(){
        cells = 0;
        box.getChildren().clear();

    }

    public void setTitle(String title){
        titleLabel = new Label(title);
        titlePanel = new HBox();
        titlePanel.setPadding(new Insets(5,5,5,5));
        Pane spacerLeft=new Pane();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        Pane spacerRight=new Pane();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        titlePanel.getChildren().addAll(spacerLeft, titleLabel,spacerRight);
        this.box = new VBox();
        this.setMinWidth(200);
        this.setMaxWidth(200);
        this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        box.setPadding(new Insets(0,0,0,0));
        box.getChildren().add(titlePanel);
        this.setContent(box);
        this.getStyleClass().add("high");
        box.getStyleClass().add("high");
        titleLabel.getStyleClass().add("high-title-label");
        titlePanel.getStyleClass().add("high-title-cell");
    }
}
