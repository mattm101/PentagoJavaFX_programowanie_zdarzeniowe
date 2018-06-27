package malecmateusz.pentago.net;
/*
jest elementem listy 10 najlepszych graczy
 */
public class HighscoreDTO {
    private String username;
    private int wins = 0;

    public HighscoreDTO(String username, int wins){
        this.username = username;
        this.wins = wins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
