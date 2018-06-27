package malecmateusz.pentagoserver.server;

import com.google.gson.Gson;
import malecmateusz.pentagoserver.app.App;
import malecmateusz.pentagoserver.database.AccountDTO;
import malecmateusz.pentagoserver.database.DatabaseController;
import malecmateusz.pentagoserver.util.ConstantsUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SessionTask implements Runnable{
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Socket socket;
	private Session session;
	private Gson gson = new Gson();
	private DatabaseController database;
	private App app;

	public SessionTask(Socket socket, App app) {
		this.socket = socket;
		this.app = app;
	}
	
	@Override
	public void run() {
		try {
			setupSession();
			LOGGER.info("Nawiązano połączenie w sesji");
		} catch (IOException e) {
			LOGGER.severe("Nie udało się nawiązać połączenia w sesji " + e.toString());

		}

		database = DatabaseController.getInstance();
		
		try {
			loginLoop();
			whileChatting();
		}catch(PrematureSessionCloseException e) {
			LOGGER.severe("Uzytkownik przedwcześnie zakończył połączenie " + e.toString());
		}
		
		
	}
	
	private void setupSession() throws IOException{
		this.session = new Session(socket);
		socket = null;
	}
	
	private void whileChatting() {
		LOGGER.info("Użytkownik jest połączony " + session.getNickname());
		DataProtocol message = null;
		boolean running = true;
		do {
			try {
				String msg = (String)session.getInput().readObject();
				message = gson.fromJson(msg, DataProtocol.class);

				switch (message.getMetadata()){
					case DataProtocol.CHAT_BROADCAST:
						SessionManager.getInstance().broadcast(session, gson.toJson(
								new DataProtocol(DataProtocol.CHAT_BROADCAST,
										session.getNickname() + ": " + (String)message.getData())
						));
						break;
					case DataProtocol.REQUEST_USER_LIST:
						SessionManager.getInstance().sendSessionList(session);
						break;
					case DataProtocol.REQUEST_CHALLANGE:
						SessionManager.getInstance().sendToOther(session,
								(String)message.getData(),
								gson.toJson(
										new DataProtocol(DataProtocol.REQUEST_CHALLANGE, session.getNickname())
								));
						break;
					case DataProtocol.RESPONSE_CHALLANGE_DECLINED:
						SessionManager.getInstance().sendToOther(session,
								(String)message.getData(),
								gson.toJson(
										new DataProtocol(DataProtocol.RESPONSE_CHALLANGE_DECLINED, session.getNickname())
								));
						break;
					case DataProtocol.RESPONSE_CHALLANGE_ACCEPTED:
						try{
							SessionManager.getInstance().setupGame(session, (String)message.getData());
							SessionManager.getInstance().broadcast(session, gson.toJson(new DataProtocol(DataProtocol.RESPONSE_CHALLANGE_DECLINED, session.getNickname())));
							SessionManager.getInstance().broadcast(session, gson.toJson(new DataProtocol(DataProtocol.RESPONSE_CHALLANGE_DECLINED, session.getOpponent())));
							//wyślij do mnie
							SessionManager.getInstance().sendToSelf(session, gson.toJson(new DataProtocol(DataProtocol.START_GAME_FIRST, session.getOpponent())));
							//wyslij do przeciwnika
							SessionManager.getInstance().sendToOther(session, session.getOpponent(), gson.toJson(new DataProtocol(DataProtocol.START_GAME, session.getNickname())));

						}catch (UnableToSetupNewGameException e){
							SessionManager.getInstance().sendToSelf(session, gson.toJson(new DataProtocol(DataProtocol.CANNOT_START_A_GAME,(String)message.getData())));
							System.out.println("Nie mozna stworzyc gry");
							LOGGER.warning("Nie można stworzyć gry " + session.getNickname() + " " + e.toString());
						}
						break;

					case DataProtocol.GAME_FIELD:
						SessionManager.getInstance().sendToOther(session, session.getOpponent(), gson.toJson(new DataProtocol(DataProtocol.GAME_FIELD, message.getData())));

						break;
					case DataProtocol.GAME_ROTATION_LEFT:
						SessionManager.getInstance().sendToOther(session, session.getOpponent(), gson.toJson(new DataProtocol(DataProtocol.GAME_ROTATION_LEFT, message.getData())));

						break;
					case DataProtocol.GAME_ROTATION_RIGHT:
						SessionManager.getInstance().sendToOther(session, session.getOpponent(), gson.toJson(new DataProtocol(DataProtocol.GAME_ROTATION_RIGHT, message.getData())));

						break;
					case DataProtocol.GAME_WIN:
						LOGGER.info(session.getNickname() + " wygrał mecz");
						database.addMatchToUser(session.getNickname(), ConstantsUtil.WON);
						SessionManager.getInstance().sendToOther(session, session.getOpponent(), gson.toJson(new DataProtocol(DataProtocol.GAME_WIN, message.getData())));

						break;
					case DataProtocol.GAME_LOSE:
						database.addMatchToUser(session.getNickname(), ConstantsUtil.LOST);
						System.out.println("GAME LOSE " + session.getNickname());
						LOGGER.info(session.getNickname() + " przegrał mecz");
						break;
					case DataProtocol.GAME_EXIT:
						//TODO:to oznacza, ze ten uzytkownik wyszedl przed zakonczeniem gry i wyslij do przeciwnika ze ja wyszedlem
						//tutaj jeszcze trzeba dodac do bazy danych ze uzytkownik wyszedle wczesniej z gry
						//jesli wyszedl przedwczesnie to drugiemu tutaj dodac zwyciestwo w bazie danych
						if(!(Boolean)message.getData()){
							database.addMatchToUser(session.getNickname(), ConstantsUtil.LEAVED);
							SessionManager.getInstance().sendToOther(session, session.getOpponent(), gson.toJson(new DataProtocol(DataProtocol.GAME_EXIT, message.getData())));
						}
						session.setOpponent(null);
						break;
					case DataProtocol.HIGHSCORES_REQUEST:
						AccountDTO accountDTO = database.getAccountDTO(session.getNickname());
						System.out.println(accountDTO);
						SessionManager.getInstance().sendToSelf(session, gson.toJson(new DataProtocol(DataProtocol.HIGHSCORES_RESPONSE, gson.toJson(accountDTO))));
						break;
				}
				//*********************************************************

				System.out.println(message);
			}catch(ClassNotFoundException e) {
				LOGGER.severe("Odebrano niezrozumiałe dane " + e.toString());
			} catch (IOException e) {
				running = false;
				LOGGER.warning("Wyjątek w pętli odbierania wiadomości " + e.toString());
				try {
					SessionManager.getInstance().removeSession(session);
					SessionManager.getInstance().broadcast(session, gson.toJson(
							new DataProtocol(DataProtocol.USER_LEAVED, session.getNickname())
					));
					app.showMessage("Usunięto sesję " + session.getNickname());
					LOGGER.info("Usunięto sesję " + session.getNickname());
				} catch (IOException e1) {
					LOGGER.severe("Błąd przy usuwaniu sesji " + session.getNickname() + " " + e.toString());
				}
			}

		}while(running);
	}
	
	
	private void loginLoop() throws PrematureSessionCloseException{
		DataProtocol data = null;
		boolean running = true;
		do {
			try {
				
				String msg = (String)session.getInput().readObject();
				data = gson.fromJson(msg, DataProtocol.class);
				switch (data.getMetadata()){
					case DataProtocol.LOGIN:
						List<String> credentials = (ArrayList<String>)data.getData();
						if(database.isUserValid(credentials.get(0), credentials.get(1))) {
							session.setNickname(credentials.get(0));
							System.out.println(1);
							SessionManager.getInstance().addSession(session);
							System.out.println(2);
							SessionManager.getInstance().broadcast(session, gson.toJson(new DataProtocol(DataProtocol.USER_ENTERED, credentials.get(0))));//TODO:tutaj zmienic dodac wiecej typow broadcastow
							running = false;
							SessionManager.getInstance().sendToSelf(session, gson.toJson(new DataProtocol(DataProtocol.LOGIN_SUCCESS, credentials.get(0))));
							app.showMessage(session.getNickname() + " zalogował się");
							LOGGER.info(session.getNickname() + " zalogował się");
						}else {
							SessionManager.getInstance().sendToSelf(session, gson.toJson(new DataProtocol(DataProtocol.LOGIN_FAILURE, null)));
						}
						break;
					case DataProtocol.REG_NEW_USER:
						credentials = (ArrayList<String>)data.getData();
						try {
							database.createNewUser(credentials.get(0), credentials.get(1));
							SessionManager.getInstance().sendToSelf(session, gson.toJson(new DataProtocol(DataProtocol.REG_SUCCESS, null)));
							app.showMessage(credentials.get(0) + " utworzył konto");
							LOGGER.info(credentials.get(0) + " utworzył konto");
						}catch(UserAlreadyExistsException e){
							SessionManager.getInstance().sendToSelf(session, gson.toJson(new DataProtocol(DataProtocol.REG_USER_EXISTS, null)));
							LOGGER.warning("Użytkownik " + credentials.get(0) + " już istnieje w bazie");
						}
						break;
				}
			} catch (ClassNotFoundException e) {
				LOGGER.severe("Odebrano niezrozumiałe dane w pętli logowania " + e.toString());
			} catch (IOException e) {
				session.close();
				running = false;
				throw new PrematureSessionCloseException();
			}
		}while(running);
	}

}
