package malecmateusz.pentago.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/*
Kwadrat z postaciami do zabicia, jego główną cechą jest obracanie się o 90 stopni w wybraną stronę
 */

public class Square extends GameObject {
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private Point[] corners = new Point[4];
    private Point[] edges = new Point[4];
    private Point centerPoint = new Point(84,84);
    private int id;
    private double pivotX;
    private double pivotY;
    private double endX;
    private double endY;
    private double width;
    private double height;
    private Image back = new Image("image/square_back4.jpg", 232,232, false, true);
    private GraphicsContext gc;
    public static final int STAY = 0, MOVE_TO_END = 1, MOVE_TO_START = 2, ROTATE_RIGHT = 3, ROTATE_LEFT = 4;
    private int objectState = STAY;
    private boolean hStartToEnd = false, vStartToEnd = false;
    private Rectangle collider;
    private double angle = 0;
    private boolean input = true;
    private boolean rotationRight = false;
    private Game game;

    public Square(Game game, int id, GraphicsContext gc, double x, double y, double width, double height, double endDeltaPosX, double endDeltaPosY){
        super(x, y);
        this.game = game;
        this.id = id;
        this.width = width;
        this.height = height;
        pivotX = x + width/2 + endDeltaPosX;
        pivotY = y + height/2 + endDeltaPosY;
        this.gc = gc;
        this.endX = x + endDeltaPosX;
        this.endY = y + endDeltaPosY;
        if(getStartX() < endX) vStartToEnd = true;
        if(getStartY() < endY) hStartToEnd = true;
        collider = new Rectangle();
        collider.setX(x);
        collider.setY(y);
        collider.setWidth(width);
        collider.setHeight(height);

        corners[0] = new Point(10,10);
        corners[1] = new Point(158,10);
        corners[2] = new Point(158,158);
        corners[3] = new Point(10,158);

        edges[0] = new Point(84,10);
        edges[1] = new Point(158,84);
        edges[2] = new Point(84,158);
        edges[3] = new Point(10,84);


    }
    public void checkForLeftClick(double x, double y) {

    }

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }

    public void update(long timestamp) {

        switch (objectState){
            case STAY:
                draw();
                break;
            case MOVE_TO_END:
                if(moveToEnd()){
                    if(rotationRight) objectState = ROTATE_RIGHT;
                    else objectState = ROTATE_LEFT;
                }
                draw();
                break;
            case MOVE_TO_START:
                if(moveToStart()){
                    objectState = STAY;
                    input = true;
                    game.setFieldInput(true);
                }
                draw();
                break;
            case ROTATE_RIGHT:
                gc.save();
                if(rotateRight()){
                    objectState = MOVE_TO_START;
                    for(Enemy enemy : enemies){
                        enemy.rotateRight();
                    }
                }
                draw();
                gc.restore();
                break;
            case ROTATE_LEFT:
                gc.save();
                if(rotateLeft()){
                    objectState = MOVE_TO_START;
                    for(Enemy enemy : enemies){
                        enemy.rotateLeft();
                    }
                }
                draw();
                gc.restore();
                break;
        }
        //draw();
    }

    private void draw(){
        //gc.setFill(Paint.valueOf("#FF0000"));
        //gc.fillRect(x, y, width, height);
        gc.drawImage(back, x,y);
        //gc.setFill(Paint.valueOf("#00FF00"));
        //gc.fillRect(pivotX, pivotY,1,1);
    }

    private boolean moveToEnd(){
        if(vStartToEnd) x += 2;
        else x -= 2;
        if(hStartToEnd) y += 2;
        else y -= 2;
        if(Math.abs(x - endX) < 4 && Math.abs(y - endY) < 4){
            x = endX;
            y = endY;
            return true;
        }
        return false;
    }

    private boolean moveToStart(){
        if(vStartToEnd) x -= 2;
        else x += 2;
        if(hStartToEnd) y -= 2;
        else y += 2;
        if(Math.abs(x - getStartX()) < 4 && Math.abs(y - getStartY()) < 4){
            x = getStartX();
            y = getStartY();
            game.setFieldInput(true);
            game.setArrowInput(false);
            return true;
        }
        return false;
    }

    private boolean rotateRight() {
        angle += 2;
        Rotate r = new Rotate(angle, pivotX, pivotY);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        if(angle >= 90){
            angle = 0;
            return true;
        }
        return false;
    }

    private boolean rotateLeft() {
        angle -= 2;
        Rotate r = new Rotate(angle, pivotX, pivotY);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        if(angle <= -90){
            angle = 0;
            return true;
        }
        return false;
    }
    public boolean getInput(){
        return input;
    }

    public double getAngle(){
        return angle;
    }

    public double getPivotX(){
        return pivotX;
    }

    public double getPivotY(){
        return pivotY;
    }

    public double getEndX(){
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public Point getCorner(int nr){
        return corners[nr];
    }

    public Point getEdge(int nr){
        return edges[nr];
    }

    public Point getCenterPoint(){
        return centerPoint;
    }

    public void setInput(boolean input){
        this.input = input;
    }

    public void setObjectState(int objectState) {
        this.objectState = objectState;
    }

    public void setRotationRight(boolean right){
        this.rotationRight = right;
    }

    public int getId() {
        return id;
    }


}
