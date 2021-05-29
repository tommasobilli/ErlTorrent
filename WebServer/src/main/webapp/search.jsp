<%@ page import="java.util.Map" %>
<%@ page import="java.text.CharacterIterator" %>
<%@ page import="java.text.StringCharacterIterator" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.Charset" %>
<%@ page import="kotlin.text.Charsets" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Search file</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="css/util.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.11.2/css/all.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/mdb.min.css">
    <!-- Plugin file -->
    <link rel="stylesheet" href="./css/addons/datatables.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<%
    String uname = (String) session.getAttribute("username");
    String address = (String) session.getAttribute("address");
    String port = (String) session.getAttribute("port");
    if (null == uname) {
        session.setAttribute("errorMessage", "Please login first");
        response.sendRedirect("index.jsp");
        return;
    } else if (null == address && null == port) {
        session.setAttribute("errorMessage", "Please add your configuration first");
        response.sendRedirect("home.jsp");
        return;
    }

%>

<body>
<jsp:include page="navbar.jsp" />
<div class="limiter">
    <c:choose>
    <c:when test="${!empty param.search}">
    <div class="container-search">
        </c:when>
        <c:otherwise>
        <div class="container-login100">
            </c:otherwise>
            </c:choose>
            <div class="wrap-login100 p-t-50 p-b-90"
                 style="width: 1000px !important; padding-top: 10px !important; padding-bottom: 10px !important;">
                <form class="login100-form validate-form flex-sb flex-w" method="get" action="SearchServlet">
					<span class="login100-form-title" style="padding-bottom: 10px !important;">
						Search file
					</span>
                    <div class="wrap-input100 m-b-16">
                        <input class="input100" type="text" name="search" placeholder="Search">
                        <span class="focus-input100"></span>
                    </div>

                    <div class="container-login100-form-btn m-t-17">
                        <button class="login100-form-btn" autofocus>
                            Search
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <div class="container">
            <%!
                public static String humanReadableByteCountBin(String filesize) {
                    try {
                        long bytes = Long.parseLong(filesize);
                        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
                        if (absB < 1024) {
                            return bytes + " B";
                        }
                        long value = absB;
                        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
                        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
                            value >>= 10;
                            ci.next();
                        }
                        value *= Long.signum(bytes);
                        return String.format("%.1f %ciB", value / 1024.0, ci.current());
                    } catch (Exception e) {
                        return "";
                    }
                }
            %>
            <%
                Map<String, Map<String, String>> searchResults = null;
                try {
                    searchResults = (Map<String, Map<String, String>>) request.getAttribute("searchResults");
                } catch (Exception e) {
                    // do nothing
                }
                if (searchResults != null && !searchResults.isEmpty()) {
                    out.print("<table id=\"dtBasicExample\" class=\"table table-striped table-bordered table-sm pt-5\" cellspacing=\"0\"\n" +
                            "                           width=\"100%\">\n" +
                            "                        <thead>");
                    out.print("<tr>\n" +
                            "                            <th class=\"th-sm\">File name\n" +
                            "                            </th>\n" +
                            "                            <th class=\"th-sm\">File size\n" +
                            "                            </th>\n" +
                            "                            <th class=\"th-sm\">Tracker address\n" +
                            "                            </th>\n" +
                            "                            <th class=\"th-sm\">Download JSON\n" +
                            "                            </th>\n" +
                            "                        </tr>\n" +
                            "                        </thead>\n" +
                            "                        <tbody>");
                    for (String filename : searchResults.keySet()) {
                        String filesize = searchResults.get(filename).get("filesize");
                        String trackerAddress = searchResults.get(filename).get("tracker_address");
                        String trackerPort = searchResults.get(filename).get("tracker_port");
                        String trackerAddrPort = trackerAddress + ":" + trackerPort;
                        String filesizeReadable = humanReadableByteCountBin(filesize);
                        out.print("<tr>");
                        out.print("<td>" + filename + "</td>");
                        out.print("<td>" + filesizeReadable + "</td>");
                        out.print("<td>" + trackerAddrPort + "</td>");
                        out.print("<td>");
                        out.print("<form method=\"post\" action=\"DownloadServlet\">");
                        out.print("<input type=\"hidden\" name=\"filename\" id=\"filename\" value=\"" + filename + "\">");
                        out.print("<input type=\"hidden\" name=\"filesize\" id=\"filesize\" value=\"" + filesize + "\">");
                        out.print("<input type=\"hidden\" name=\"tracker_address\" id=\"tracker_address\" value=\"" + trackerAddress + "\">");
                        out.print("<input type=\"hidden\" name=\"tracker_port\" id=\"tracker_port\" value=\"" + trackerPort + "\">");
                        out.print("<button type=\"submit\"><i class=\"fa fa-download mr-2 grey-text\" aria-hidden=\"true\"></i></button> >1 kB</td></form>");
                    }
                    out.print("</tbody>\n" +
                            "                        <tfoot>\n" +
                            "                        <tr>\n" +
                            "                            <th>File name\n" +
                            "                            </th>\n" +
                            "                            <th>File size\n" +
                            "                            </th>\n" +
                            "                            <th>Tracker address\n" +
                            "                            </th>\n" +
                            "                            <th>Download JSON\n" +
                            "                            </th>\n" +
                            "                        </tr>\n" +
                            "                        </tfoot>\n" +
                            "                    </table>");
                } else {

                    out.print("<div class=\"col-12 text-center\" style=\"position: relative; top: -20px\">\n" +
                            "                        <div class=\"p-0 m-0\">\n" +
                            "                            <img alt=\"No results\" src=\"img/no-search-result.svg\" width=\"400\" height=\"400\">\n" +
                            "                        </div>\n" +
                            "                        <h4>Sorry, that search produced no results.</h4>\n" +
                            "                        <h4>Please try with different keywords.</h4>\n" +
                            "                    </div>");

                }
            %>

        </div>
    </div>

    <!--===============================================================================================-->
    <script src="vendor/jquery/jquery-3.2.1.min.js"></script>
    <!--===============================================================================================-->
    <script src="vendor/animsition/js/animsition.min.js"></script>
    <!--===============================================================================================-->
    <script src="vendor/bootstrap/js/popper.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
    <!--===============================================================================================-->
    <script src="vendor/select2/select2.min.js"></script>
    <!--===============================================================================================-->
    <script src="vendor/daterangepicker/moment.min.js"></script>
    <script src="vendor/daterangepicker/daterangepicker.js"></script>
    <!--===============================================================================================-->
    <script src="vendor/countdowntime/countdowntime.js"></script>
    <!--===============================================================================================-->
    <script src="js/main.js"></script>

    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/popper.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/mdb.min.js"></script>
    <!-- Plugin file -->
    <script src="./js/addons/datatables.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#dtBasicExample').DataTable({
                "searching": false
            });
            $('.dataTables_length').addClass('bs-select');
        });
    </script>

</body>
</html>