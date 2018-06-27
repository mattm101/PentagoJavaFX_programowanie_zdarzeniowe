package malecmateusz.pentagoserver.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import malecmateusz.pentagoserver.server.Server;
import malecmateusz.pentagoserver.util.PentagoLogger;

import java.io.IOException;


public class App extends Application {
	private Image icon;
	private Server server;
	Thread serverThread;
	Stage window;
	private TextArea statusMessages;
	public static void main( String[] args ) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		try{
			PentagoLogger.setup();
		}catch(IOException e){
			e.printStackTrace();
			throw new RuntimeException("Logger nie wystartował");
		}
		statusMessages = new TextArea();
		statusMessages.setWrapText(true);
		statusMessages.setEditable(false);
		server = new Server(this);
		serverThread = new Thread(server);
		serverThread.start();

		icon = new Image(App.class.getClassLoader().getResourceAsStream("resources/image/icon.png"));

		StackPane root = new StackPane();
		root.getChildren().addAll(statusMessages);

		window = primaryStage;
		window.setTitle("PENTAGO SERVER");
		window.getIcons().add(icon);
		window.setScene(new Scene(root));
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if(server != null) server.stop();
				window.close();
				System.exit(0);
			}
		});

		window.show();

	}

	public void showMessage(final String msg){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusMessages.appendText(msg + "\n");
			}
		});
	}
}
