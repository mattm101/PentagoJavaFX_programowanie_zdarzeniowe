package malecmateusz.pentagoserver.database;

import java.util.ArrayList;
import java.util.List;

public class AccountDTO {
    private int wins = 0;
    private int loses = 0;
    private int leaves = 0;
    private List<HighscoreDTO> highscores = new ArrayList<HighscoreDTO>();

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getLeaves() {
        return leaves;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public List<HighscoreDTO> getHighscores() {
        return highscores;
    }

    public void setHighscores(List<HighscoreDTO> highscores) {
        this.highscores = highscores;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("\nWINS: ").append(wins)
                .append("\nLOSES: ").append(loses)
                .append("\nLEAVES: ").append(leaves);
        for(HighscoreDTO h : highscores){
            builder.append("\nNICKNAME: ").append(h.getUsername())
                    .append(" WINS: ").append(h.getWins());
        }
        return builder.toString();
    }

    public void sort(){
        quicksort(0, highscores.size() - 1);
    }

    private void quicksort(int x, int y) {

        int i,j,v;
        HighscoreDTO temp;

        i = x;
        j = y;
        v=highscores.get((x + y) / 2).getWins();
        do {
            while (highscores.get(i).getWins()<v)
                i++;
            while (v<highscores.get(j).getWins())
                j--;
            if (i<=j) {
                temp=highscores.get(i);
                highscores.set(i, highscores.get(j));
                highscores.set(j,temp);
                i++;
                j--;
            }
        }
        while (i<=j);
        if (x<j)
            quicksort(x,j);
        if (i<y)
            quicksort(i,y);
    }

    public void trim(int value){
        List<HighscoreDTO> temp= new ArrayList<HighscoreDTO>();
        if(highscores.size() < value){
            value = highscores.size();
        }

        for(int i=value - 1 ;i >= 0;i--){
            temp.add(highscores.get(i));
        }
        highscores = temp;
    }
}
