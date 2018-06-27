package malecmateusz.pentago.game;

/*
komórka na której może stać przeciwnik do zabicia, przechowuje swój stan,
EMPTY oznacza żywego przeciwnika
 */

public class Field {
    public static final int EMPTY = 0, YOU = 1, OPPONENT = 2;
    private volatile int value = EMPTY;
    private Enemy enemy;
    private final int id;

    public Field(int id){
        this.id = id;
    }

    public void setValue(int val){
        this.value = val;
    }

    public int getValue(){
        return value;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public int getId() {
        return id;
    }
}
