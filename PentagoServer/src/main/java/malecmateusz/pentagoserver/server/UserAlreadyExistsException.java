package malecmateusz.pentagoserver.server;

public class UserAlreadyExistsException extends RuntimeException {
    private String username;
    public UserAlreadyExistsException(String username){
        super();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
