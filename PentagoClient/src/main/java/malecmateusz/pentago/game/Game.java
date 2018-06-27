package malecmateusz.pentago.game;

import malecmateusz.pentago.lang.LangController;
import malecmateusz.pentago.net.DataListener;
import malecmateusz.pentago.net.DataProtocol;
import malecmateusz.pentago.net.NetworkController;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class Game implements DataListener {
    public static final int TOP = 0, BOTTOM = 1, RIGHT = 2, LEFT = 3;
    private boolean yourTurn = true;
    private boolean fieldInput = true;
    private boolean arrowInput = false;
    private boolean gameEnd = false;
    private GameText gameText;

    private final Field[][] gameboard = new Field[6][6];
    private List<Square> squares = new ArrayList<Square>();
    public Game(boolean first, GameText gameText){
        int id = 0;
        this.gameText = gameText;
        for(int y=0;y<6;y++){
            for(int x=0;x<6;x++){
                gameboard[y][x] = new Field(id++);
            }
        }
        if(first){
            yourTurn = true;
            gameText.setText(LangController.getInstance().getActiveBean().getGameYourTurn());
            gameText.setObjectState(GameText.START);
        }
        else{
            yourTurn = false;
        }
        NetworkController.getInstance().addDataListener(this);
    }

    public Field getField(int y, int x){
        return gameboard[y][x];
    }

    public void rotateBoard(int partVertical, int partHorizontal, int direction){
        int startVertical, startHorizontal, endVertical, endHorizontal;
        if(partVertical == TOP) startVertical = 0;
        else startVertical = 3;
        if(partHorizontal == LEFT) startHorizontal = 0;
        else  startHorizontal = 3;

        endVertical = startVertical + 2;
        endHorizontal = startHorizontal + 2;

        if(direction == RIGHT){
            Field tmp = gameboard[startVertical][startHorizontal];
            gameboard[startVertical][startHorizontal] = gameboard[endVertical][startHorizontal];
            gameboard[endVertical][startHorizontal] = gameboard[endVertical][endHorizontal];
            gameboard[endVertical][endHorizontal] = gameboard[startVertical][endHorizontal];
            gameboard[startVertical][endHorizontal] = tmp;
            tmp = gameboard[startVertical][startHorizontal + 1];
            gameboard[startVertical][startHorizontal + 1] = gameboard[startVertical + 1][startHorizontal];
            gameboard[startVertical + 1][startHorizontal] = gameboard[endVertical][endHorizontal - 1];
            gameboard[endVertical][endHorizontal - 1] = gameboard[endVertical - 1][endHorizontal];
            gameboard[endVertical - 1][endHorizontal] = tmp;
        } else{
            Field tmp = gameboard[startVertical][startHorizontal];
            gameboard[startVertical][startHorizontal] = gameboard[startVertical][endHorizontal];
            gameboard[startVertical][endHorizontal] = gameboard[endVertical][endHorizontal];
            gameboard[endVertical][endHorizontal] = gameboard[endVertical][startHorizontal];
            gameboard[endVertical][startHorizontal] = tmp;
            tmp = gameboard[startVertical][startHorizontal + 1];
            gameboard[startVertical][startHorizontal + 1] = gameboard[startVertical + 1][endHorizontal];
            gameboard[startVertical + 1][endHorizontal] =  gameboard[endVertical][endHorizontal - 1];
            gameboard[endVertical][endHorizontal - 1] = gameboard[startVertical + 1][startHorizontal];
            gameboard[startVertical + 1][startHorizontal] = tmp;
        }

        if(checkForWin()){
            gameText.setText(LangController.getInstance().getActiveBean().getGameWin());
            gameText.setObjectState(GameText.MOVE);
            yourTurn = false;
            fieldInput = false;
            arrowInput = false;
            gameEnd = true;
            NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.GAME_WIN,null));
            NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.REQUEST_USER_LIST,null));

        }
    }

    private boolean checkForWin(){
        boolean win = false;
        for(int i=0;i<6;i++){
            int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0, count10 = 0, count11 = 0, count12 = 0;
            for(int j=0;j<5;j++){
                if(gameboard[i][j].getValue() == Field.YOU) count1++;
                if(gameboard[i][j+1].getValue()== Field.YOU) count2++;
                if(gameboard[j][i].getValue() == Field.YOU) count3++;
                if(gameboard[j+1][i].getValue() == Field.YOU) count4++;
                if(gameboard[j][j].getValue() == Field.YOU) count5++;
                if(gameboard[j+1][j+1].getValue() == Field.YOU) count6++;
                if(gameboard[j][5-j].getValue() == Field.YOU) count7++;
                if(gameboard[j+1][4-j].getValue() == Field.YOU) count8++;
                if(gameboard[j][j+1].getValue() == Field.YOU) count9++;
                if(gameboard[j+1][j].getValue() == Field.YOU) count10++;
                if(gameboard[j+1][5-j].getValue() == Field.YOU) count11++;
                if(gameboard[j][4-j].getValue() == Field.YOU) count12++;
            }
            if(count1 == 5 || count2 == 5 || count3 == 5 || count4 == 5
                    || count5 == 5 || count6 == 5 || count7 == 5 || count8 == 5 || count9 == 5
                    || count10 == 5 || count11 == 5 || count12 == 5) win = true;
        }
        return win;
    }

    public void show(){
        for(int i=0;i<6;i++){
            System.out.println();
            for(int j=0;j<6;j++) System.out.print(gameboard[i][j].getValue() + " ");
        }
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public boolean isFieldInput() {
        return fieldInput;
    }

    public void setFieldInput(boolean fieldInput) {
        this.fieldInput = fieldInput;
    }

    public boolean isArrowInput() {
        return arrowInput;
    }

    public void setArrowInput(boolean arrowInput) {
        this.arrowInput = arrowInput;
    }


    @Override
    public void onDataRecieved(final DataProtocol data) {
        switch (data.getMetadata()){
            case DataProtocol.GAME_FIELD:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int id = Integer.valueOf((String)data.getData());
                        for(int i=0;i<6;i++) for(int j=0;j<6;j++){
                            if(gameboard[i][j].getId() == id){
                                gameboard[i][j].getEnemy().setOpponent(true);
                                gameboard[i][j].setValue(Field.OPPONENT);
                                gameboard[i][j].getEnemy().setObjectState(Enemy.BOMB);
                                System.out.println("Odebrano id " + id);
                                setFieldInput(false);
                            }
                        }
                    }
                });
                break;
            case  DataProtocol.GAME_ROTATION_LEFT:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int id = Integer.valueOf((String)data.getData());
                        squares.get(id).setRotationRight(false);
                        squares.get(id).setObjectState(Square.MOVE_TO_END);
                        yourTurn = true;
                        arrowInput = false;
                        if(!gameEnd) {
                            gameText.setText(LangController.getInstance().getActiveBean().getGameYourTurn());
                            gameText.setObjectState(GameText.START);
                        }

                    }
                });
                break;
            case  DataProtocol.GAME_ROTATION_RIGHT:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int id = Integer.valueOf((String)data.getData());
                        squares.get(id).setRotationRight(true);
                        squares.get(id).setObjectState(Square.MOVE_TO_END);
                        yourTurn = true;
                        arrowInput = false;
                        if(!gameEnd) {
                            gameText.setText(LangController.getInstance().getActiveBean().getGameYourTurn());
                            gameText.setObjectState(GameText.START);
                        }
                    }
                });
                break;
            case  DataProtocol.GAME_WIN:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        yourTurn = false;
                        fieldInput = false;
                        arrowInput = false;
                        gameEnd = true;
                        gameText.setText(LangController.getInstance().getActiveBean().getGameLose());
                        gameText.setObjectState(GameText.START);
                        NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.GAME_LOSE,null));
                        NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.REQUEST_USER_LIST,null));
                    }
                });
                break;
            case DataProtocol.GAME_EXIT:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        yourTurn = false;
                        fieldInput = false;
                        arrowInput = false;
                        gameEnd = true;
                        gameText.setText(LangController.getInstance().getActiveBean().getGameLeaved());
                        gameText.setObjectState(GameText.START);
                        //NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.GAME_LOSE,null));
                        NetworkController.getInstance().sendData(new DataProtocol(DataProtocol.REQUEST_USER_LIST,null));
                    }
                });
                break;

        }
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public boolean isGameEnd(){
        return gameEnd;
    }
}
