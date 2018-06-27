package malecmateusz.pentago.game;

import malecmateusz.pentago.net.DataProtocol;
import malecmateusz.pentago.net.NetworkController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/*
Przeciwnik do zabica w grze, w swoim postawowoym stanie losuje liczbę od 1 do 120, ponieważ
gra odświerza się 60 razy na sekundę, jeżeli wylosuje 1, wyświetla animację obrotu przeciwnika,
oprócz tego klasa zapewnia metodę obrotu przeciwnika na platformie
 */

public class Enemy extends GameObject {

    public static final int CORNER = 0, EDGE = 1, CENER = 2;
    Random rand;
    private Field field;
    private int fieldNumber;
    private int fieldType;
    private final String FLAME_RED_PATH = "image/flame.png";
    private final String FLAME_GREEN_PATH = "image/flame_green.png";
    private final String IDLE_PATH = "image/sheet_hero_idle.png";
    private final String FLY_PATH = "image/sheet_hero_fly.png";
    private final String DEAD_PATH = "image/sheet_hero_dead.png";
    private final String EXP_PATH = "image/exp.png";
    private final String BOMB_PATH = "image/bomb.png";
    private double bombX;
    private double bombY;
    public static final int IDLE = 0, FLY = 1, DEAD = 2, FLAME = 3, EXPLOSION = 4, BOMB = 5;
    private malecmateusz.pentago.game.Animation idleAnim;
    private malecmateusz.pentago.game.Animation flyAnim;
    private malecmateusz.pentago.game.Animation deadAnim;
    private malecmateusz.pentago.game.Animation flameRedAnim;
    private malecmateusz.pentago.game.Animation flameGreenAnim;
    private malecmateusz.pentago.game.Animation explosionAnim;
    private malecmateusz.pentago.game.Animation bombAnim;
    private Rectangle collider;
    private volatile int objectState = IDLE;
    private Square square;
    private boolean input = true;
    private boolean opponent = false;
    private Game game;

    public  Enemy(Game game,GraphicsContext gc, double x, double y, Square square, int fieldNumber, int fieldType, Field field){
        super(x,y);
        this.game = game;
        this.field = field;
        this.square = square;
        this.fieldNumber = fieldNumber;
        this.fieldType = fieldType;

        idleAnim = new Animation(gc, new Image(IDLE_PATH), 8, 800000000);
        flyAnim  = new Animation(gc, new Image(FLY_PATH), 4,  400000000);
        deadAnim = new Animation(gc, new Image(DEAD_PATH), 8, 800000000);
        flameRedAnim = new Animation(gc, new Image(FLAME_RED_PATH, 384, 64, false, false),
                6, 600000000);
        flameGreenAnim = new Animation(gc, new Image(FLAME_GREEN_PATH, 384, 64, false, false),
                6, 600000000);
        explosionAnim = new Animation(gc, new Image(EXP_PATH,896,64, false, false),
                14, 800000000);
        bombAnim = new Animation(gc, new Image(BOMB_PATH,192,64, false, false),
                3, 300000000);
        collider = new Rectangle();
        System.out.println("X " + x + " y " + y);
        collider.setX(square.getX() + x +15);
        collider.setY(square.getY() + y + 30);
        collider.setWidth(idleAnim.getFrameWidth() - 24);
        collider.setHeight(idleAnim.getFrameHeight() - 30);
        //wspolzedne musza byc aktualizowane po obrocie square
        bombX = square.getX() + x;
        bombY = -64;
        rand = new Random();
        updatePos();

    }

    public void checkForLeftClick(double x, double y) {
        if(collider.contains(x,y) && game.isYourTurn() && game.isFieldInput() && !game.isGameEnd()){
            objectState = BOMB;
            game.setFieldInput(false);
            //game.setArrowInput(false);
            opponent = false;
            input = false;
            field.setValue(Field.YOU);
            NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.GAME_FIELD, Integer.toString(field.getId())));
        }
    }


    public int getObjectState() {
        return objectState;
    }

    public void setObjectState(int objectState) {
        this.objectState = objectState;
    }

    public void update(long timestamp) {
        if(square.getAngle() != 0) onRotation();
        switch (objectState){
            case IDLE:
                if(rand.nextInt(120) + 1 == 1) objectState = FLY;
                else idleAnim.render(timestamp, square.getX() + super.getX(), square.getY() +super.getY());
                break;
            case BOMB:
                idleAnim.render(timestamp, square.getX() +super.getX(), square.getY() +super.getY());
                bombY += 5;
                bombAnim.render(timestamp, bombX, bombY);
                if(square.getY() + getY() - bombY < 40){
                    objectState = EXPLOSION;
                    game.setArrowInput(true);
                }
                break;
            case EXPLOSION:
                if(opponent)flameRedAnim.render(timestamp, square.getX() +super.getX(), square.getY() +super.getY());
                else flameGreenAnim.render(timestamp, square.getX() +super.getX(), square.getY() +super.getY());
                boolean ended = explosionAnim.render(timestamp, square.getX() +super.getX(), square.getY() +super.getY());
                if(ended){
                    objectState = FLAME;
                }
                break;
            case FLAME:
                if(opponent)flameRedAnim.render(timestamp, square.getX() +super.getX(), square.getY() +super.getY());
                else flameGreenAnim.render(timestamp, square.getX() +super.getX(), square.getY() +super.getY());
                break;
            case FLY:
                if(flyAnim.render(timestamp, square.getX() +super.getX(), square.getY() +super.getY())) objectState = IDLE;
                break;
        }
    }

    private void onRotation(){

        double deltaX = ((getStartX() + 32 + square.getX() -square.getPivotX())*Math.cos(Math.toRadians(square.getAngle()))) - ((getStartY() + 32 + square.getY() - square.getPivotY())*Math.sin(Math.toRadians(square.getAngle())));
        double deltaY = ((getStartX() + 32 + square.getX() -square.getPivotX())*Math.sin(Math.toRadians(square.getAngle()))) + ((getStartY() + 32 + square.getY() - square.getPivotY())*Math.cos(Math.toRadians(square.getAngle())));
        x = deltaX + square.getPivotX() - square.getX() - 32;
        y = deltaY + square.getPivotY() - square.getY() - 32;

    }

    public void rotateRight(){
        fieldNumber++;
        if(fieldNumber > 3) fieldNumber = 0;
        updatePos();
    }

    public void rotateLeft(){
        fieldNumber--;
        if(fieldNumber < 0) fieldNumber = 3;
        updatePos();
    }

    public void updatePos(){
        Point point;
        if(fieldType == CORNER) point = square.getCorner(fieldNumber);
        else if(fieldType == EDGE) point = square.getEdge(fieldNumber);
        else point = square.getCenterPoint();
        bombX = point.x + square.getStartX();
        x = startX = point.x;
        y = startY = point.y;
        collider.setX(square.getStartX() + x +15);
        collider.setY(square.getStartY() + y + 30);
    }

    public void setOpponent(boolean opponent) {
        this.opponent = opponent;
    }
}
