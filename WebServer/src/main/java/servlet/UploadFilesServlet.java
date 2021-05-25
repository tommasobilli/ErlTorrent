package servlet;

import exceptions.FileAlreadyUploadedException;
import interfaces.ISingletonErlangBean;
import interfaces.IUserBean;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
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

        String filename = request.getParameter("filename");
        HttpSession session = request.getSession(true);
        String pid = (String) session.getAttribute("pid");
        String username = (String) session.getAttribute("username");
        String address = (String) session.getAttribute("address");
        boolean fileIsPresent = false;
        try {
            fileIsPresent = erlangServerBean.verifyPreviousUploads(filename,pid);
        } catch (NullPointerException | FileAlreadyUploadedException e) {
            logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
            response.sendRedirect("upload.jsp");
            session=request.getSession(true);
            session.setAttribute("errorMessage", "The user has already uploaded this file");
            return;
        }
        if (fileIsPresent) {
            logger.info("[DEBUG] File is present");
            try {
                erlangServerBean.addUsertoTracker(filename, username, pid, address);
            } catch(Exception e) {

            }
        }
        else {
            logger.info("[DEBUG] File is not present");
        }

    }
}
