package servlet;
import exceptions.IncorrectPasswordException;
import exceptions.UserNotFoundException;
import interfaces.IUserBean;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.logging.Logger;
import entities.User;


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
