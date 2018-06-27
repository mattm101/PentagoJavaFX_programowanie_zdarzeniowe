package malecmateusz.pentago.game;

/*
wszystkie obiekty w grze muszą dziedziczyć po tej klasie, zapewnia ona,
że będą mogły zostać wyrenderowane przez managera obiektów,
porzechowuje aktualną pozycję obiekut i jego pozycję startową
 */

public abstract class GameObject {
    protected double x;
    protected double y;
    protected double startX;
    protected double startY;

    public GameObject(double x, double y){
        this.x = startX = x;
        this.y = startY = y;
    }

    public GameObject(double x, double y, double startX, double startY){
        this.x = x;
        this.y = y;
        this.startX = startX;
        this.startY = startY;
    }

    public abstract void checkForLeftClick(double x, double y);

    public abstract void update(long timestamp);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }
}
