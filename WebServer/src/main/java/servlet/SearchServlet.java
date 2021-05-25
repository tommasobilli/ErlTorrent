package servlet;

import entities.User;
import exceptions.PasswordNotMatchException;
import exceptions.UserNotFoundException;
import interfaces.IFilesBean;
import interfaces.IUserBean;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;


@WebServlet(name = "SearchServlet", value = "/SearchServlet")
public class SearchServlet extends HttpServlet {

    @EJB
    IFilesBean filesBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside the service method of SearchServlet");

        String search = req.getParameter("search");
        Map<String, Map<String, String>> searchResults = filesBean.getFilesInfo(search);
        req.setAttribute("searchResults", searchResults);

        String search_ = URLEncoder.encode(search, StandardCharsets.UTF_8.toString());
        RequestDispatcher rd = req.getRequestDispatcher("search.jsp?search=" + search_);
        rd.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {}

}