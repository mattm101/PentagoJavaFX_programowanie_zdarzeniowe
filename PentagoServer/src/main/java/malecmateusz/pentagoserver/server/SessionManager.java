package malecmateusz.pentagoserver.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
Manager sesji, zarządza listą aktualnie zalogowanych użytkowników
 */
public class SessionManager {
	private static SessionManager instance;
	private Object sessionsLock;
	private List<Session> sessions;
	
	private SessionManager() {
		sessions = new ArrayList<Session>();
		sessionsLock = new Object();
	}
	
	public synchronized static SessionManager getInstance() {
		if(instance == null) instance = new SessionManager();
		return instance;
	}
	
	public void addSession(Session session) {
		synchronized(sessionsLock) {
			sessions.add(session);
		}
	}

	public void setupGame(Session mySession, String opponentsNickname) throws UnableToSetupNewGameException{
		Session foundMe = null;
		Session foundOpponent = null;
		synchronized (sessionsLock){
			for(Session s : sessions){
				if(s.getNickname().equals(opponentsNickname)){
					if(s.getOpponent() == null) foundOpponent = s;
				}
				else if(s == mySession && s.getOpponent() == null) foundMe = mySession;
			}
			if(foundMe == null || foundOpponent == null) throw new UnableToSetupNewGameException();

		}
		mySession.setOpponent(opponentsNickname);
		foundOpponent.setOpponent(mySession.getNickname());
	}


	
	public void removeSession(Session session) throws IOException {
		synchronized(sessionsLock) {
			sessions.remove(session);
		}
			session.close();
		
	}
	
	public void broadcast(Session session, Object data) throws IOException {
		synchronized(sessionsLock) {
			for(Session s : sessions) {
				if(s != session) {
					s.getOutput().writeObject(data);
					s.getOutput().flush();
				}
				
			}
		}
	}
	
	public void sendToSelf(Session session, Object data) throws IOException {
		synchronized(sessionsLock) {
			session.getOutput().writeObject(data);
			session.getOutput().flush();
		}
	}

	public void sendSessionList(Session session) throws IOException {
		List<String> users = new ArrayList<String>();
		synchronized (sessionsLock){
			for(Session s : sessions){
				if(s != session) users.add(s.getNickname());
			}
			session.getOutput().writeObject(new Gson().toJson(new DataProtocol(DataProtocol.RESPONSE_USER_LIST, users)));
			session.getOutput().flush();
		}
	}
	
	public void sendToOther(Session session, String targetNick, Object data) throws IOException {
		synchronized(sessionsLock) {
			for(Session s : sessions) {
				if(s.getNickname().equals(targetNick)) {
					s.getOutput().writeObject(data);
					s.getOutput().flush();
				}
			}	
		}
	}
	
}
