package malecmateusz.pentago.game;

/*
Interfejs mediatora dla managera obiektów, pozwala on hurtowo sprawdzać kolizje na obiektach
i je odrysowywać
 */

public interface GameObjectMediator {
    void update(long timestamp);
    void checkForLeftClick(double x, double y);
}
