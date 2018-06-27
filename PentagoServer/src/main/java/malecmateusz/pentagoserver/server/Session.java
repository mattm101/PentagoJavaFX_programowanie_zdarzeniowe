package malecmateusz.pentagoserver.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;
/*
obiekt sesji, przechowuje gniazdo połączenia z klientem, oraz jeżeli jest on zalogowany, jego pseudonim
 */
public class Session {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String nickname = null;
	private String opponent = null;
	private Object opponentLock = new Object();
	
	public Session(Socket socket) throws IOException {
		this.socket = socket;
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
		output.flush();
	}
	
	public Session(Socket socket, ObjectOutputStream output, ObjectInputStream input, String nickname) {
		this.socket = socket;
		this.output = output;
		this.input = input;
		this.nickname = nickname;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public void close() {
		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			LOGGER.warning("Błąd zamknięcia sesji " + nickname + " " + e.toString());
			e.printStackTrace();
		}
		
	}
	
	
	
}
