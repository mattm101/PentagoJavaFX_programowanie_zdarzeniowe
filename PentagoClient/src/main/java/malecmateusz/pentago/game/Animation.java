package malecmateusz.pentago.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/*
Klasa animacji, pozwala na animowanie skewencji spritów, które są w jednym pliku,
pozwala na ustalenie czasu animacji, rozkłąda ilość klatek równomiernie na czas animacji
 */
public class Animation {
    private int frames;
    private double frameWidth;
    private double frameHeight;
    private Image image;
    private long durationPerFrame;
    private long animationTime = 0;
    private int frame = 0;
    private boolean ended = false;
    private GraphicsContext gc;
    private boolean rotated = false;

    public Animation(GraphicsContext gc, Image image, int frames, long duration){
        this.image = image;
        this.gc = gc;
        frameWidth = image.getWidth() / frames;
        frameHeight = image.getHeight();
        durationPerFrame = duration/frames;
        this.frames = frames;

    }
    public Animation(GraphicsContext gc, Image image, int frames, long duration, boolean rotated){
        this.image = image;
        this.gc = gc;
        frameWidth = image.getWidth() / frames;
        frameHeight = image.getHeight();
        durationPerFrame = duration/frames;
        this.frames = frames;
        this.rotated = rotated;

    }
    //do wyswietlania pierwszego wiersza spritesheetow, ktore mają kilka wierszy
    public Animation(GraphicsContext gc, Image image, int frames, long duration, double frameHeight){
        this.image = image;
        this.gc = gc;
        frameWidth = image.getWidth()/frames;
        this.frameHeight = frameHeight;
        durationPerFrame = duration/frames;
        this.frames = frames;
    }

    public boolean render(long timestamp, double x, double y){
        ended = false;
        animationTime += timestamp;
        if(animationTime > durationPerFrame){
            animationTime = 0;
            frame++;
            if(frame >= frames){
                frame  = 0;
                ended = true;
            }
        }
        gc.drawImage(image, frame * frameWidth, 0, frameWidth, frameHeight, x,y, frameWidth, frameHeight);
        //gc.setStroke(Paint.valueOf("#FFFFFF"));
        //gc.strokeRect(x,y,64,64);
        //gc.stroke();
        return ended;
    }

    public void restart(){
        frame = 0;
        animationTime = 0;
        ended = false;
    }

    public double getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(double frameWidth) {
        this.frameWidth = frameWidth;
    }

    public double getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(double frameHeight) {
        this.frameHeight = frameHeight;
    }

    public GraphicsContext getGc() {
        return gc;
    }

}
