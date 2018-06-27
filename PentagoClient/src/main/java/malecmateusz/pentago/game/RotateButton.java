package malecmateusz.pentago.game;

import malecmateusz.pentago.net.DataProtocol;
import malecmateusz.pentago.net.NetworkController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
/*
obiekt przycisku ze strzałką, wywołuje on obrót jednego z czterech kwadratów
 */
public class RotateButton extends GameObject{
    private GraphicsContext gc;
    private boolean right;
    Rectangle collider = new Rectangle();
    Square square;
    Game game;
    private static final String BUTTON_UP = "image/button_up.png";
    private static final String BUTTON_DOWN = "image/button_down.png";
    private Image image;
    int partV;
    int partH;
    int dir;
    private double width = 40, height = 40;
    public RotateButton(GraphicsContext gc, double x, double y, boolean right, Square square, Game game, int partV, int partH, int dir, boolean up){
        super(x,y);
        this.game = game;
        this.partH = partH;
        this.partV = partV;
        this.dir = dir;
        collider.setX(x);
        collider.setY(y);
        collider.setWidth(width);
        collider.setHeight(height);
        this.gc = gc;
        this.right = right;
        this.square = square;
        if(up) image = new Image(BUTTON_UP, 40,40, false, true);
        else image = new Image(BUTTON_DOWN, 40,40, false, true);
    }
    public void checkForLeftClick(double x, double y) {
        if(collider.contains(x,y) && game.isYourTurn() && game.isArrowInput() && !game.isGameEnd()){
            square.setInput(false);
            square.setObjectState(Square.MOVE_TO_END);
            square.setRotationRight(right);
            game.rotateBoard(partV, partH, dir);
            game.show();
            game.setArrowInput(false);
            game.setYourTurn(false);

            if(right)NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.GAME_ROTATION_RIGHT, Integer.toString(square.getId())));
            else NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.GAME_ROTATION_LEFT, Integer.toString(square.getId())));
        }
    }

    public void update(long timestamp) {
        gc.drawImage(image, x,y);
    }
}
