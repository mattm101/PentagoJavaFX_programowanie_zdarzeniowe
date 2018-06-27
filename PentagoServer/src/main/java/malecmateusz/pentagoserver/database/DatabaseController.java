package malecmateusz.pentagoserver.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import malecmateusz.pentagoserver.server.UserAlreadyExistsException;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

/*
klasa do zarządzania bazą danych
 */
public class DatabaseController {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static DatabaseController instance;
    private SessionFactory factory;
    private Object factoryLock;
    private DatabaseController(){
        factoryLock = new Object();
    }

    public static DatabaseController getInstance(){
        if(instance == null) instance = new DatabaseController();
        return instance;
    }

    public void connectToDatabase(){

        URL file = DatabaseController.class.getClassLoader().getResource("resources/hibernate.cfg.xml");

        factory = new Configuration().configure(file)
                .addAnnotatedClass(UserEntity.class)
                .addAnnotatedClass(MatchEntity.class)
                .buildSessionFactory();
    }

    public void addMatchToUser(String username, int matchStatus){
        Session session;
        synchronized (factoryLock){
            session = factory.openSession();
        }

        session.beginTransaction();
        UserEntity user = (UserEntity) session.createQuery("from UserEntity u where u.username='" + username + "'").getSingleResult();
        user = session.get(UserEntity.class, user.getId());
        MatchEntity match = new MatchEntity(matchStatus);
        user.add(match);
        session.save(match);
        session.getTransaction().commit();
        session.close();
    }

    public void createNewUser(String username, String password) throws UserAlreadyExistsException {
        Session session;
        synchronized (factoryLock) {
            session = factory.openSession();
        }
        session.beginTransaction();
        List<UserEntity> users = session.createQuery("from UserEntity u where u.username='" + username+"'").getResultList();
        if(users.size() > 0) throw new UserAlreadyExistsException(username);
        UserEntity user = new UserEntity(username, password);
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    public boolean isUserValid(String username, String password){
        Session session;
        synchronized (factoryLock){
            session = factory.openSession();
        }
        List<UserEntity> users = session.createQuery("from UserEntity u where u.username='"
        + username +"' AND u.password='"
        + password + "'").getResultList();
        session.close();
        if(users.size() == 0) return false;
        return true;
    }

    public AccountDTO getAccountDTO(String username){
        AccountDTO accountDTO = new AccountDTO();
        Session session;
        synchronized (factoryLock){
            session = factory.openSession();
        }
        List<UserEntity> users = session.createQuery("from UserEntity").getResultList();
        int count = 0;
        for(UserEntity user : users){
            int wins = ((Long) session.createQuery("select count(*) from MatchEntity m where m.userEntity.id='" + user.getId() + "'" + " AND m.matchStatus=0").uniqueResult()).intValue();
            accountDTO.getHighscores().add(new HighscoreDTO(user.getUsername(), wins));
            if(user.getUsername().equals(username)){
                int loses = ((Long)session.createQuery("select count(*) from MatchEntity m where m.userEntity.id='" + user.getId()+ "'" +" AND m.matchStatus=1").uniqueResult()).intValue();
                int leaves = ((Long)session.createQuery("select count(*) from MatchEntity m where m.userEntity.id='" + user.getId()+ "'" +" AND m.matchStatus=2").uniqueResult()).intValue();
                accountDTO.setWins(wins);
                accountDTO.setLeaves(loses);
                accountDTO.setLeaves(leaves);
            }

        }
        session.close();
        accountDTO.sort();
        accountDTO.trim(10);
        return accountDTO;
    }

}
