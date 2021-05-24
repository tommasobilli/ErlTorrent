package interfaces;

import entities.User;
import exceptions.PortNotCorrectException;
import exceptions.UserNotFoundException;

import javax.ejb.Remote;

@Remote
public interface IUserBean {

    User getUser(String username) throws UserNotFoundException;

    User createUser(User user);

    void setAddressAndPort(String address, String port, String username) throws PortNotCorrectException;

}
