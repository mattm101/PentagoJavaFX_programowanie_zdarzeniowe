package malecmateusz.pentagoserver.database;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/*
klasa tabeli użytkownika, przechowuje pseudonim użytkownika oraz jego hasło,
a także referencję do tabeli przechowującej wyniki meczy w relacji jeden-do-wielu
 */
@Entity
@Table(name="fxuser")
public class UserEntity {
    @Id
    @Column(name="fxuser_id")
    private int id;
    @Column(name="fxuser_username")
    private String username;
    @Column(name="fxuser_password")
    private String password;
    @OneToMany(mappedBy = "userEntity",
    cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<MatchEntity> matches;

    public UserEntity(){}

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<MatchEntity> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchEntity> matches) {
        this.matches = matches;
    }

    public void add(MatchEntity match){
        if(matches == null) matches = new ArrayList<MatchEntity>();
        matches.add(match);
        match.setUserEntity(this);
    }
}
