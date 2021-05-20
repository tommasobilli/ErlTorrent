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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            // DA QUI IN POI L'UTENTE È LOGGATO DUNQUE VIENE REINDIRIZZATO ALLA PAGINA IN CUI HA DUE SCELTE
            // 1. Scaricare un nuovo file
            // 2. Uplodare un nuovo file => Iscriversi quindi ad un nuovo tracker.


        } catch (UserNotFoundException| IncorrectPasswordException e) {
            logger.info("[DEBUG] LoginServlet EXCEPTION: " + e.getMessage());
            req.setAttribute("errorMessage", "Incorrect username and/or password");
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch(Exception e) {
            logger.info("[DEBUG] UNKNOWN EXCEPTION: " + e.getMessage());
        }
    }
}
