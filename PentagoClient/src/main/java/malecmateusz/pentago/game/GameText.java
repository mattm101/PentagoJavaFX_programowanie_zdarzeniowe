package malecmateusz.pentago.game;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
/*
Obiekt powiadomień o nowej turze, wygranej lup rzegranej, albo o wyjściu drugiego gracza z gry
renderuje on czerwoną wstążkę z tekstem
 */
public class GameText extends GameObject {
    public static final int START = 0, MOVE = 1, SLOWER = 2, FAST = 3, END = 4, IDLE = 5;
    private volatile int objectState = IDLE;
    private Image back = new Image("image/text_back.png", 400, 100, true, true);
    private volatile String text;
    private GraphicsContext gc;


    public GameText(GraphicsContext gc, double x, double y, double endX, double endY){
        super(x,y,endX,endY);
        this.gc = gc;
    }
    @Override
    public void checkForLeftClick(double x, double y) {

    }

    @Override
    public void update(long timestamp) {
        gc.setFont(new Font("Arial", 20));
        switch (getObjectState()){
            case IDLE:
                break;
            case START:
                x = -400;
                objectState = MOVE;
                break;
            case MOVE:
                if(x < startX) x += 10;
                else objectState = SLOWER;
                drawText();
                break;
            case END:
                drawText();
                break;

            case SLOWER:
                if(x < startX + 70) x += 2;
                else objectState = FAST;
                drawText();
                break;
            case FAST:
                if(x < 800) x += 7;
                else{
                    objectState = IDLE;
                    x = -400;
                }
               drawText();
                break;

        }
    }

    private void drawText(){
        gc.drawImage(back,x,y);
        gc.setFill(Paint.valueOf("#FFFFFF"));
        Text tx = new Text(text);
        Bounds tb = tx.getBoundsInLocal();
        Rectangle stencil = new Rectangle(
                tb.getMinX(), tb.getMinY(), tb.getWidth(), tb.getHeight()
        );

        Shape intersection = Shape.intersect(tx, stencil);

        Bounds ib = intersection.getBoundsInLocal();
        gc.fillText(text, x + 200.0f - (ib.getWidth()), y + 40);
        //System.out.println("szerokosc tekstu " + ib.getWidth());
    }

    public synchronized int getObjectState() {
        return objectState;
    }

    public synchronized void setObjectState(int objectState) {
        this.objectState = objectState;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
