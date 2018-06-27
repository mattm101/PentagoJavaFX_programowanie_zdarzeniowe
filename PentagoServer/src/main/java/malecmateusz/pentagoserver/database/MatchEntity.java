package malecmateusz.pentagoserver.database;


import javax.persistence.*;

/*
tabela przechowująca wynik meczu dla danego gracza
 */

@Entity
@Table(name="fxmatch")
public class MatchEntity {
    @Id
    @Column(name="fxmatch_id")
    private int id;
    @Column(name="fxmatch_status")
    private int matchStatus;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="fxmatch_fxuser_id")
    private UserEntity userEntity;

    public MatchEntity(int matchStatus){
        this.matchStatus = matchStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
