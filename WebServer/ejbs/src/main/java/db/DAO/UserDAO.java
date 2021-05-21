package db.DAO;

import com.mongodb.client.MongoCollection;
import db.IUserDAO;
import db.dbConnector;
import db.entities.dbUser;
import entities.User;
import exceptions.UserNotFoundException;

import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO implements IUserDAO {

    private final MongoCollection<dbUser> collection;

    public UserDAO(dbConnector instance) {
        this.collection = instance.getDatabase().getCollection("users", dbUser.class);
    }

    @Override
    public User getUser(String username) throws UserNotFoundException {
        dbUser user = collection.find(eq("username", username)).first();

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        return dbUserToUser(user);
    }

    protected User dbUserToUser(dbUser user) {

        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside dbUserToUser");

        if (user == null) {
            return null;
        }

        String pid = null;
        String username = user.getUsername();
        String pwd = user.getPwd();
        String APIToken = user.getToken();
        String address = user.getAddress();
        String listeningPort = user.getListeningPort();

        if (user.getId() != null) {
            pid = user.getId().toHexString();
        }

        return new User(pid, username, pwd, APIToken, address, listeningPort);
    }
}
