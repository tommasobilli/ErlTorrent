package servlet;

import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;


@WebServlet(name = "DownloadServlet", value = "/DownloadServlet")
public class DownloadServlet extends HttpServlet {

    static class JsonBuilder {
        public final JsonObject json = new JsonObject();

        public String toJson() {
            return json.toString();
        }

        public JsonBuilder add(String key, String value) {
            json.addProperty(key, value);
            return this;
        }

        public JsonBuilder add(String key, JsonBuilder value) {
            json.add(key, value.json);
            return this;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] inside the service method of DownloadServlet");

        String filename = req.getParameter("filename");
        String filesize = req.getParameter("filesize");
        String trackerAddress = req.getParameter("tracker_address");
        String trackerPort = req.getParameter("tracker_port");

        HttpSession session = req.getSession(false);
        String pid = (String) session.getAttribute("pid");
        String address = (String) session.getAttribute("address");
        String port = (String) session.getAttribute("port");
        String API_token = (String) session.getAttribute("API_token");

        if (filename != null && filesize != null && trackerAddress != null && trackerPort != null && pid != null && address != null && port != null) {
            logger.info("i'm heere");
            String json = new JsonBuilder()
                    .add("pid", pid)
                    .add("API_token", API_token)
                    .add("listeningPort", port)
                    .add("trackerAddress", trackerAddress)
                    .add("FileName", filename)
                    .add("FileSize", filesize)
                    .add("ContainsFile", "0")
                    .toJson();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=" + filename + ".json");
            resp.getWriter().write(json);
        } else {
            resp.sendRedirect("home.jsp");
        }
    }

}

