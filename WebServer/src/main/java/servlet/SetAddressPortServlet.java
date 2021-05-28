package servlet;

import exceptions.PortNotCorrectException;
import exceptions.AddressNotValidException;
import interfaces.IUserBean;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "SetAddressPortServlet", value = "/SetAddressPortServlet")
public class SetAddressPortServlet extends HttpServlet {

    @EJB
    IUserBean userBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside the service method of SetAddressPortServlet");

        String address = request.getParameter("address");
        try {
            if (!address.matches("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$"))
                throw new AddressNotValidException();
        } catch (AddressNotValidException e) {
            logger.info("[DEBUG] LoginServlet EXCEPTION: " + e.getMessage());
            response.sendRedirect("home.jsp");
            HttpSession  session=request.getSession(true);
            session.setAttribute("errorMessage", "Not valid IP address");
            return;
        }
        String port = request.getParameter("port");
        try {
            HttpSession session = request.getSession(true);
            if (Integer.valueOf(port).intValue() <= 1024 || Integer.valueOf(port).intValue() >= 49151 || port.matches("[a-zA-Z]+"))
                throw new PortNotCorrectException((String) session.getAttribute("username"));
            userBean.setAddressAndPort(address, port, (String) session.getAttribute("username"));
            session.setAttribute("address", address);
            logger.info("[DEBUG] " + session.getAttribute("address"));
            session.setAttribute("port", port);
            response.sendRedirect("home.jsp");

        } catch (PortNotCorrectException | NumberFormatException e) {
            logger.info("[DEBUG] LoginServlet EXCEPTION: " + e.getMessage());
            response.sendRedirect("home.jsp");
            HttpSession  session=request.getSession(true);
            session.setAttribute("errorMessage", "Port already in use or not valid");
        }
    }
}
