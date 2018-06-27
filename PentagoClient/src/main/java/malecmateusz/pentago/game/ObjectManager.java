package malecmateusz.pentago.game;

import java.util.ArrayList;
import java.util.List;

/*
Manager obiektów w grze, renderuje wszystkie istniejące obiekty w grze, oraz wywołuje
sprawdzenie kolizji przy kliknieciu myszą w polu okna gry
 */

public class ObjectManager implements GameObjectMediator {
    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    public void update(long timestamp) {
        for(GameObject go : gameObjects){
            go.update(timestamp);
        }
    }

    public void checkForLeftClick(double x, double y) {
        for(GameObject go : gameObjects){
            go.checkForLeftClick(x, y);
        }
    }

    public void add(GameObject gameObject){
        gameObjects.add(gameObject);
    }

    public void remove(GameObject gameObject){
        gameObjects.remove(gameObject);
    }
}
