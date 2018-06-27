package malecmateusz.pentago.components.highscore;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
/*
komórka listy 10 najlepszych graczy
 */
public class HighscoreCell extends HBox {
    private String nickname;
    private int score;
    private int nr;
    private Label nrLabel;
    private Label nickLabel;
    private Label scoreLabel;

    public HighscoreCell(int nr, String nickname, int score){
        this.nickname = nickname;
        this.score = score;
        this.nr = nr;
        Pane spacer=new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10,1);
        this.setMinWidth(200);
        this.setSpacing(10);
        this.setPadding(new Insets(10,10,10,10));
        StringBuilder builder = new StringBuilder();
        if(nr < 10) builder.append("  ");
        this.nrLabel = new Label(builder.append(Integer.toString(nr)).append(".").toString());
        this.nickLabel = new Label(nickname);
        this.scoreLabel = new Label(Integer.toString(score));
        scoreLabel.setPadding(new Insets(0,20,0,0));
        this.getChildren().addAll(nrLabel, nickLabel,spacer, scoreLabel);
        this.getStyleClass().add("high-cell");
        nrLabel.getStyleClass().add("high-cell-label");
        nickLabel.getStyleClass().add("high-cell-label");
        scoreLabel.getStyleClass().add("high-cell-label");

    }
}
