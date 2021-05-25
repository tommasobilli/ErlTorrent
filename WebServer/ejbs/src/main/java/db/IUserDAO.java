package db;

import entities.User;
import exceptions.PortNotCorrectException;
import exceptions.UserNotFoundException;

public interface IUserDAO {

    User getUser(String username) throws UserNotFoundException;

    User createUser(User user);

    void setAddressAndPort(String address, String port, String username) throws PortNotCorrectException;

    String getUserPort(String pid);

}

