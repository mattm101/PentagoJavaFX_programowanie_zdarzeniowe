package malecmateusz.pentago.components.popupmenu;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;

/*
menu ustawień, w zamiarach miało być budowane przez buildera, ale nie zdążę już tego zrobić,
pozwala na dodawanie elementów do siebie przez method chaining,
 */

public class PopMenu extends Popup{
    private VBox box = new VBox();
    private int el = 0;
    public PopMenu(){
        super();
        setAutoHide( true );
        setHideOnEscape( true );
        setAutoFix( true );
        box.getStyleClass().add("pop-menu");
    }

    public PopMenu addElement(Node element){
        el++;
        element.getStyleClass().add("pop-element");
        box.getChildren().add(element);
        return this;
    }

    public PopMenu addLabel(String text){
        Label label = new Label(text);
        label.getStyleClass().add("pop-label");
        box.getChildren().add(label);
        return this;
    }
    //dodaje poziomy brzydki separator
    public PopMenu addSeparator(){
        Line line = new Line();
        Rectangle rec = new Rectangle();
       Bounds bounds = box.getChildren().get(el - 1).getBoundsInLocal();
       line.setStartX(bounds.getWidth()-180);
       line.setStartY(bounds.getHeight());
       line.setEndX(bounds.getWidth()-10);
       line.setEndY(bounds.getHeight());
       rec.setX(bounds.getWidth()-180);
       rec.setY(bounds.getHeight());
       rec.setWidth(180);
       rec.setHeight(2);


        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#4b5a72")),
                new Stop(0.4, Color.web("#6182b7")),
                new Stop(0.6, Color.web("#6182b7")),
                new Stop(1,Color.web("#4b5a72"))
        };
        LinearGradient linearGradient =
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        line.setFill(linearGradient);
        rec.setFill(linearGradient);
        Group g = new Group(rec);
        g.getStyleClass().add("pop-separator");
       box.getChildren().add(g);
       el++;
       return this;
    }

    public void build(){
        this.getContent().add(box);
    }

    public void showBelow(Node node, Pane pane){
        Point2D point = node.localToScene(0.0,  0.0);
        show(node,pane.getScene().getWindow().getX() + point.getX() - 128,
                pane.getScene().getWindow().getY() + point.getY() + 75);
    }

}
