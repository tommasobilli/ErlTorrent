package servlet;

import entities.User;
import exceptions.PasswordNotMatchException;
import exceptions.UserNotFoundException;
import interfaces.IUserBean;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;


@WebServlet(name = "SignupServlet", value = "/SignupServlet")
public class SignupServlet extends HttpServlet {

    @EJB
    IUserBean userBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside the service method of SignupServlet");

        String username = req.getParameter("username_s");
        String pwd = req.getParameter("pass_s");
        String pwdConfirm = req.getParameter("pass_confirm_s");

        try{

            if(!pwd.equals(pwdConfirm)){
                throw new PasswordNotMatchException(username);
            }

            User user = userBean.getUser(username);

            logger.info("[DEBUG] SignupServlet User with username: " + user.getUsername() + "Already Exists");
            resp.sendRedirect("signup.jsp");
            HttpSession  session=req.getSession(true);
            session.setAttribute("ErrorMessage", "ERROR: Existing username");

        }catch(PasswordNotMatchException e){

            logger.info("[DEBUG] SignupServlet PasswordNotMatchException: " + e.getMessage());
            resp.sendRedirect("signup.jsp");
            HttpSession  session=req.getSession(true);
            session.setAttribute("ErrorMessage", "ERROR: The Password does not match");

        }catch (UserNotFoundException e){

            logger.info("[DEBUG] SignupServlet UserNotFoundException: " + e.getMessage());
            String computed_hash = utils.password.hashPassword(pwd);

            //Variabile Globale settata da pannello admin: server.config=>JVM Settings=>JVMOPTIONS
            String jwt_secret = System.getProperty("JWT_SECRET");

            String jwt = Jwts.builder()
                    .claim("username", username)
                    .signWith(
                            SignatureAlgorithm.HS256,
                            jwt_secret.getBytes("UTF-8")
                    )
                    .compact();

            logger.info("[DEBUG] SignupServlet JWT Computed: " + jwt);

            User user = new User(username, computed_hash, jwt);
            userBean.createUser(user);

            resp.sendRedirect("signup.jsp");
            HttpSession  session=req.getSession(true);
            session.setAttribute("SuccessMessage", "User correctly created");

        }
    }

}
