package db;

import entities.User;
import exceptions.UserNotFoundException;

public interface IUserDAO {

    User getUser(String username) throws UserNotFoundException;

    User createUser(User user);

}
