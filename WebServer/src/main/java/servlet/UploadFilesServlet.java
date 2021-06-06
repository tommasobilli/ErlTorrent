package servlet;

import exceptions.FileAlreadyUploadedException;
import exceptions.FileNotAddedException;
import exceptions.NotSuccededInsertion;
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

    void downloadConfig (String filename, String filesize, String trackerAddrPort, String pid, String port, String API_token, HttpServletResponse resp) throws IOException {
        if (filename != null && filesize != null && trackerAddrPort != null && pid != null  && port != null) {
            String json = new DownloadServlet.JsonBuilder()
                    .add("pid", pid)
                    .add("API_token", API_token)
                    .add("listeningPort", port)
                    .add("tracker_addr_port", trackerAddrPort)
                    .add("FileName", filename)
                    .add("FileSize", filesize)
                    .add("ContainsFile", "1")
                    .toJson();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=" + filename + ".json");
            resp.getWriter().write(json);
        } else {
            resp.sendRedirect("home.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside the service method of UploadFileServlet");
        HttpSession session = request.getSession(true);
        String filename = request.getParameter("filename");
        String size = request.getParameter("size");
        /*if (!size.matches("[0-9]+")) {
            logger.info("[DEBUG] Unknown size");
            response.sendRedirect("upload.jsp");
            session =request.getSession(true);
            session.setAttribute("errorMessage", "The size of the file is not valid");
            return;
        } */
        String pid = (String) session.getAttribute("pid");
        String listeningPort = (String) session.getAttribute("port");
        String username = (String) session.getAttribute("username");
        String address = (String) session.getAttribute("address");
        String API_token = (String) session.getAttribute("API_token");
        //session.setAttribute("pid", pid);
        boolean successful = false;
        try {
            successful = erlangServerBean.verifyPreviousUploadsAndInsert(filename, pid, API_token, username, address, size);
        } catch (FileAlreadyUploadedException e) {
            logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
            response.sendRedirect("upload.jsp");
            session=request.getSession(true);
            session.setAttribute("errorMessage", "The user has already uploaded this file");
            return;
        }catch (ConnectException e) {
            logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
            response.sendRedirect("upload.jsp");
            session = request.getSession(true);
            session.setAttribute("errorMessage", "Connection problems to Erlang server occur!");
            return;
        } catch (NotSuccededInsertion | FileNotAddedException e) {
            logger.info("[DEBUG] Unknown EXCEPTION: " + e.getMessage());
            session = request.getSession(true);
            session.setAttribute("errorMessage", "Insertion has not been possible");
            response.sendRedirect("upload.jsp");
            return;
        } catch(Exception e) {}
        if (successful) {
            logger.info("[DEBUG] Insertion successful!");
            String trackerAddrAndPort = erlangServerBean.getTrackerAddrAndPort(filename);
            this.downloadConfig(filename, size, trackerAddrAndPort, pid, listeningPort, API_token, response);
        }
        else {
                logger.info("[DEBUG] Unknown EXCEPTION: Not successful insertion");
                session=request.getSession(true);
                session.setAttribute("errorMessage", "The file has not been inserted correctly");
                response.sendRedirect("upload.jsp");
            }
        }
    }
