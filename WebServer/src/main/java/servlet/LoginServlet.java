package servlet;
import entities.User;
import exceptions.IncorrectPasswordException;
import exceptions.UserNotFoundException;
import interfaces.IUserBean;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;


@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {


    @EJB
    IUserBean userBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside the service method of LoginServlet");

        String username = req.getParameter("username");
        String pwd = req.getParameter("pass");
        try {

            User user = userBean.getUser(username);
            boolean Authorised = utils.password.checkPassword(pwd, user.getPwd());
            if(!Authorised){
                throw new IncorrectPasswordException(username);
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("address", user.getAddress());
            session.setAttribute("pid", user.getPid());
            logger.info("[DEBUG] address: " + user.getAddress());
            logger.info("[DEBUG] address: " + user.getListeningPort());
            session.setAttribute("port", user.getListeningPort());
            resp.sendRedirect("home.jsp");

        } catch (UserNotFoundException| IncorrectPasswordException e) {
            logger.info("[DEBUG] LoginServlet EXCEPTION: " + e.getMessage());
            resp.sendRedirect("index.jsp");
            HttpSession  session=req.getSession(true);
            session.setAttribute("errorMessage", "Incorrect username and/or password");
        } catch (Exception e){
            logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
            resp.sendRedirect("index.jsp");
            HttpSession  session=req.getSession(true);
            session.setAttribute("errorMessage", "Internal Server Error");
        }
    }
}
