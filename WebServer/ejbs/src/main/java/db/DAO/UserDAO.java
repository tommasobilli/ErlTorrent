package db.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import db.IUserDAO;
import db.dbConnector;
import db.entities.dbUser;
import entities.User;
import exceptions.PortNotCorrectException;
import exceptions.UserNotFoundException;
import org.bson.types.ObjectId;

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

    @Override
    public void setAddressAndPort(String address, String port, String username) throws PortNotCorrectException {
        dbUser lis = collection.find(Filters.and(Filters.eq("listening_port", port), Filters.eq("address", address))).first();

        if (lis != null) {
            throw new PortNotCorrectException(username);
        }
        collection.updateOne(Filters.eq("username", username),  Updates.set("listening_port", port));
        collection.updateOne(Filters.eq("username", username),  Updates.set("address", address));
    }

    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        dbUser dbUser = UserToDbUser(user);

        collection.insertOne(dbUser);

        return dbUserToUser(dbUser);
    }

    private dbUser UserToDbUser(User user) {

        if (user == null) {
            return null;
        }

        ObjectId id = null;
        String username = user.getUsername();
        String pwd = user.getPwd();
        String token = user.getAPIToken();
        String address = null;
        String listeningPort = null;

        return new dbUser(null, pwd, username, token, null, null);

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
