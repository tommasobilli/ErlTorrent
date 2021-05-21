package ejbs;

import javax.ejb.Stateless;

import db.DAO.UserDAO;
import db.IUserDAO;
import db.dbConnector;
import entities.User;
import exceptions.UserNotFoundException;
import interfaces.IUserBean;

//@Stateless(name = "UserEJB")
@Stateless
public class UserBean implements IUserBean{

    private final IUserDAO iUserDAO = new UserDAO(dbConnector.getInstance());

    public UserBean() {
    }

    @Override
    public User getUser(String username) throws UserNotFoundException {
        return iUserDAO.getUser(username);
    }

    @Override
    public User createUser(User user) {
        return iUserDAO.createUser(user);
    }

}
