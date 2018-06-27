package malecmateusz.pentagoserver.server;

import malecmateusz.pentagoserver.app.App;
import malecmateusz.pentagoserver.config.ConfigManager;
import malecmateusz.pentagoserver.database.DatabaseController;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
/*
głowny serwer aplikacji, przyjmuje nowe zgłoszenia klientów i uruchamia do ich obsługi nowe wątki SessionTask
 */
public class Server implements Runnable{
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private volatile boolean running = true;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private ServerSocket server;
	private Socket connection;
	private ConfigManager config;
	private DatabaseController database;
	private App app;

	public Server(App app){
		this.app = app;
	}
	
	public void startRunning() {
		try {
			app.showMessage("Uruchamianie...");
			config = ConfigManager.getInstance();
			LOGGER.info("Wczytano plik konfiguracyjny");
			database = DatabaseController.getInstance();
			database.connectToDatabase();
			LOGGER.info("Połączono z bazą danych");
			int port = Integer.valueOf ((String)config.getValue("PORT"));
			server = new ServerSocket(port, 100);
			LOGGER.info("Otworzono port serwera " + port);
			app.showMessage("Gotowe!");
			while(running) {
				try {
					app.showMessage("Czekam na nowe połączenie...");
					LOGGER.info("Czekam na nowe połączenie");
					waitForConnection();
				
				}catch(EOFException e) {
					app.showMessage("Serwer zakończył pracę");
					LOGGER.warning("Serwer zakończył pracę");
				}
			}
		}catch(IOException e) {
			app.showMessage("Błąd przy uruchamianiu serwera!");
			LOGGER.severe("Błąd przy uruchamianiu serwera " + e.toString());
			running = false;
		}
	}
	
	private void waitForConnection() throws IOException{
		
		Socket socket = server.accept();
		new Thread(new SessionTask(socket, app)).start();
		app.showMessage("Akceptuje nowe połączenie z " + socket.getInetAddress());
		LOGGER.info("Akceptuje nowe połączenie z " + socket.getInetAddress());
	}

	@Override
	public void run() {
		startRunning();
	}

	public void stop(){
		running = false;
	}
}
