package malecmateusz.pentago.core;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

/*
Efekt przejścia, dodawany do paneli gry i statystyk gracza
 */

public class Effect{
    public static void fadeIn(Node node){
        FadeTransition ft = new FadeTransition(Duration.millis(400), node);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }
}
