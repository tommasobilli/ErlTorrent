package interfaces;

import javax.ejb.Remote;
import entities.User;
import exceptions.UserNotFoundException;

@Remote
public interface IUserBean {

    User getUser(String username) throws UserNotFoundException;

    User createUser(User user);

}
