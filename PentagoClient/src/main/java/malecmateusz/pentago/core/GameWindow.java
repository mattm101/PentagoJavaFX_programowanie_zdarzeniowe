package malecmateusz.pentago.core;

import malecmateusz.pentago.game.*;
import malecmateusz.pentago.net.DataProtocol;
import malecmateusz.pentago.net.NetworkController;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/*
okno gry, tworzy wszystkie obiekty w grze, oraz uruchamia główną pętlę gry
 */

public class GameWindow extends Group {
    private Canvas canvas;
    private final ObjectManager objectManager = new ObjectManager();
    private GraphicsContext gc;
    private int fps = 0;
    private long prevTime = 0;
    private long sec = 0;
    Game game;
    private Image gameBack = new Image("image/back8.jpg", 3840,2160, false, false);
    final Stage stage;
    final Scene scene;
    private final AnimationTimer timer;

    public GameWindow(boolean first, final String nickname){
        canvas = new Canvas(700,650);
        gc = canvas.getGraphicsContext2D();
        GameText gameText = new GameText(gc, -400, 200, 50,200);
        game = new Game(first, gameText);
        stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Pentago - " + nickname);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.GAME_EXIT,game.isGameEnd()));
                System.out.println("GAME END " + nickname);
                stage.close();
                timer.stop();
            }
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        gc.scale(1,1);
        this.getChildren().add(canvas);

        Square topLeft = new Square(game,0,gc, 100,100, 232,232, -50, -50);

        Square topRight = new Square(game,1,gc, 350,100, 232,232, 50, -50);

        Square bottomRight = new Square(game,2,gc, 350,350, 232,232, 50, 50);

        Square bottomLeft = new Square(game,3,gc, 100,350, 232,232, -50, 50);

        game.getSquares().add(topLeft);
        game.getSquares().add(topRight);
        game.getSquares().add(bottomRight);
        game.getSquares().add(bottomLeft);

        objectManager.add(topLeft);
        objectManager.add(topRight);
        objectManager.add(bottomLeft);
        objectManager.add(bottomRight);

        createEnemies(topLeft, 1);
        createEnemies(topRight, 2);
        createEnemies(bottomRight, 3);
        createEnemies(bottomLeft, 4);

        createLeftButtonsPair(topLeft, 50,100, Game.TOP, Game.LEFT);
        createButtonsPair(topRight, 592, 100, Game.TOP, Game.RIGHT);
        createLeftButtonsPair(bottomLeft, 50, 502, Game.BOTTOM, Game.LEFT);
        createButtonsPair(bottomRight, 592, 502, Game.BOTTOM, Game.RIGHT);

        objectManager.add(gameText);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long timestamp = now - prevTime;
                prevTime = now;
                sec += timestamp;
                fps++;
                if(sec >= 1000000000){
                    System.out.println("FPS " + fps);
                    fps = 0;
                    sec = 0;
                }
               // gc.setFill(Paint.valueOf("#000000"));
               // gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
                gc.drawImage(gameBack,0,0,canvas.getWidth(),canvas.getHeight());
                objectManager.update(timestamp);
                //drawLine(gc);
            }
        };

        scene = new Scene(this, 700,650);


        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    objectManager.checkForLeftClick(event.getX(), event.getY());
                }
            }
        });
        stage.setScene(scene);
        stage.show();
        timer.start();
    }

    private void createEnemies(Square square, int nr){
        int snx = 0, sny = 0;
        if(nr == 1) snx = sny = 0;
        if(nr == 2){
            snx = 3;
            sny = 0;
        }
        if(nr ==3){
            snx = 3;
            sny = 3;
        }
        if(nr == 4){
            snx = 0;
            sny = 3;
        }
        Enemy topLeft = new Enemy(game,gc, 0,0, square,0, Enemy.CORNER, game.getField(sny, snx));
        game.getField(sny, snx).setEnemy(topLeft);
        Enemy topRight = new Enemy(game,gc, 0,0, square,1, Enemy.CORNER, game.getField(sny, snx + 2));
        game.getField(sny, snx + 2).setEnemy(topRight);
        Enemy bottomRight = new Enemy(game,gc, 0,0, square,2, Enemy.CORNER, game.getField(sny + 2, snx + 2));
        game.getField(sny + 2, snx + 2).setEnemy(bottomRight);
        Enemy bottomLeft = new Enemy(game,gc, 0,0, square,3, Enemy.CORNER, game.getField( sny + 2, snx));
        game.getField( sny + 2, snx).setEnemy(bottomLeft);

        Enemy topCenter = new Enemy(game,gc, 0,0, square,0, Enemy.EDGE, game.getField(sny,snx + 1));
        game.getField(sny,snx + 1).setEnemy(topCenter);
        Enemy rightCenter = new Enemy(game,gc, 0,0, square,1, Enemy.EDGE, game.getField(sny+1, snx+2));
        game.getField(sny+1, snx+2).setEnemy(rightCenter);
        Enemy bottomCenter = new Enemy(game,gc, 0,0, square,2, Enemy.EDGE,game.getField(sny+2, snx+1));
        game.getField(sny+2, snx+1).setEnemy(bottomCenter);
        Enemy leftCenter = new Enemy(game,gc, 0,0, square,3, Enemy.EDGE, game.getField(sny+1, snx));
        game.getField(sny+1, snx).setEnemy(leftCenter);

        Enemy center = new Enemy(game,gc, 0,0, square,0, Enemy.CENER, game.getField(sny+1, snx+1));
        game.getField(sny+1, snx+1).setEnemy(center);

        objectManager.add(topLeft);
        objectManager.add(topRight);
        objectManager.add(bottomLeft);
        objectManager.add(bottomRight);
        objectManager.add(topCenter);
        objectManager.add(rightCenter);
        objectManager.add(bottomCenter);
        objectManager.add(leftCenter);
        objectManager.add(center);
        square.addEnemy(topLeft);
        square.addEnemy(topRight);
        square.addEnemy(bottomLeft);
        square.addEnemy(bottomRight);
        square.addEnemy(topCenter);
        square.addEnemy(rightCenter);
        square.addEnemy(bottomCenter);
        square.addEnemy(leftCenter);
        square.addEnemy(center);

    }

    public void createLeftButtonsPair(Square square, double x, double y, int partV,int partH){
        RotateButton rotateLeft = new RotateButton(gc, x,y, true, square, game, partV, partH, Game.LEFT, true);
        RotateButton rotateRight = new RotateButton(gc, x, y + 40, false, square,game, partV, partH, Game.RIGHT, false);
        objectManager.add(rotateRight);
        objectManager.add(rotateLeft);
    }


    public void createButtonsPair(Square square, double x, double y, int partV,int partH){
        RotateButton rotateLeft = new RotateButton(gc, x,y, false, square, game, partV, partH, Game.LEFT, true);
        RotateButton rotateRight = new RotateButton(gc, x, y + 40, true, square,game, partV, partH, Game.RIGHT, false);
        objectManager.add(rotateRight);
        objectManager.add(rotateLeft);
    }

    private void drawLine(GraphicsContext gc){
        double sX = 300, sY = 300, rotatedX = 300, rotatedY = 300;
        double pX = 320, pY = 320;

        double tmpX = sX - pX;
        double tmpY = sY - pY;
        double angle = 0;
        rotatedX = tmpX*Math.cos(Math.toRadians(angle)) - tmpY*Math.sin(Math.toRadians(angle)) + pX;
        rotatedY = tmpX*Math.sin(Math.toRadians(angle)) + tmpY*Math.cos(Math.toRadians(angle)) + pY;


        gc.setStroke(Paint.valueOf("#00FF00"));
        gc.strokeLine(rotatedX, rotatedY, pX, pY);

    }
}
