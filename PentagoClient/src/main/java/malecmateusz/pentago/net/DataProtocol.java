package malecmateusz.pentago.net;

import java.io.Serializable;

public class DataProtocol implements Serializable{
	public static final int LOGIN = 0;
	public static final int CHAT_BROADCAST = 1;
	public static final int LOGIN_SUCCESS = 2;
	public static final int LOGIN_FAILURE = 3;
	public static final int REG_SUCCESS = 4;
	public static final int REG_USER_EXISTS = 5;
	public static final int REG_NEW_USER = 6;
	public static final int USER_ENTERED = 7;
	public static final int USER_LEAVED = 8;
	public static final int LOGOUT = 9;
	public static final int REQUEST_USER_LIST = 10;
	public static final int RESPONSE_USER_LIST = 11;
	public static final int REQUEST_CHALLANGE = 12;
	public static final int RESPONSE_CHALLANGE_ACCEPTED = 13;
	public static final int RESPONSE_CHALLANGE_DECLINED = 14;
	public static final int START_GAME = 15;
	public static final int CANNOT_START_A_GAME = 16;
	public static final int START_GAME_FIRST = 17;
	public static final int GAME_FIELD = 18;
	public static final int GAME_ROTATION_LEFT = 19;
	public static final int GAME_ROTATION_RIGHT = 20;
	public static final int GAME_WIN = 21;
	public static final int GAME_LOSE = 22;
	public static final int GAME_EXIT = 23;
	public static final int HIGHSCORES_REQUEST = 24;
	public static final int HIGHSCORES_RESPONSE = 25;

	private final int metadata;
	private Object data = null;

	public DataProtocol(final int metadata) {
		this.metadata = metadata;
	}

	public DataProtocol(final int metadata, Object data) {
		this.metadata = metadata;
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getMetadata() {
		return metadata;
	}



}
