package servlet;

import exceptions.FileAlreadyUploadedException;
import exceptions.FileNotAddedException;
import exceptions.UserNotFoundException;
import interfaces.ISingletonErlangBean;
import interfaces.IUserBean;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Logger;

@WebServlet(name = "UploadFilesServlet", value = "/UploadFilesServlet")

public class UploadFilesServlet extends HttpServlet {

    @EJB
    ISingletonErlangBean erlangServerBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside the service method of UploadFileServlet");
        HttpSession session = request.getSession(true);
        String filename = request.getParameter("filename");
        String size = request.getParameter("size");
        if (!size.matches("[0-9]+")) {
            logger.info("[DEBUG] Unknown size");
            response.sendRedirect("upload.jsp");
            session =request.getSession(true);
            session.setAttribute("errorMessage", "The size of the file is not valid");
            return;
        }
        String pid = (String) session.getAttribute("pid");
        String username = (String) session.getAttribute("username");
        String address = (String) session.getAttribute("address");
        session.setAttribute("pid", pid);
        boolean fileIsPresent = false;
        try {
            fileIsPresent = erlangServerBean.verifyPreviousUploads(filename, pid);
        } catch (FileAlreadyUploadedException e) {
            logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
            response.sendRedirect("upload.jsp");
            session=request.getSession(true);
            session.setAttribute("errorMessage", "The user has already uploaded this file");
            return;
        }catch (ConnectException e) {
            logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
            response.sendRedirect("upload.jsp");
            session=request.getSession(true);
            session.setAttribute("errorMessage", "Connection problems to Erlang server occur!");
            return;
        }
        if (fileIsPresent) {
            logger.info("[DEBUG] File is present");
            boolean insertion = erlangServerBean.addUsertoTracker(filename, username, pid, address);
            if (!insertion) {
                session = request.getSession(true);
                session.setAttribute("errorMessage", "Insertion has not been possible");
            }
            else { logger.info("[DEBUG] Insertion successful!");}
            response.sendRedirect("home.jsp");
        }
        else {
            logger.info("[DEBUG] File is not present");
            try {
                erlangServerBean.assignToTrackerAndInsert(filename, username, pid, address, size);
            } catch(FileNotAddedException e) {
                logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
                response.sendRedirect("upload.jsp");
                session=request.getSession(true);
                session.setAttribute("errorMessage", "The file has not been inserted correctly beacuse of connection problems");
            }
            logger.info("[DEBUG] File has been correctly inserted");
            response.sendRedirect("home.jsp");
        }

    }
}
