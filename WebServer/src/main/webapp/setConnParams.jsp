<%--
  Created by IntelliJ IDEA.
  User: andreadidonato
  Date: 21/05/21
  Time: 23:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Set Address and Port</title>
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
</head>
</head>

<%
    String uname = (String) session.getAttribute("username");
    if (null == uname) {
        session.setAttribute("errorMessage", "Please login first");
        response.sendRedirect("index.jsp");
    }
%>

<body>
<jsp:include page="navbar.jsp" />
<div class="container">
    <div class="row">
        <div class="limiter">
            <div class="container-login100">
                <div class="wrap-login100 p-t-50 p-b-90">
                    <form class="login100-form validate-form flex-sb flex-w" method="post" action="SetAddressPortServlet">
                            <span class="login100-form-title p-b-51">
                                Set Address and Port
                            </span>
                        <div class="wrap-input100 validate-input m-b-16" data-validate = "Address is required">
                            <input class="input100" type="text" name="address" placeholder="Address" required>
                            <span class="focus-input100"></span>
                        </div>
                        <div class="wrap-input100 validate-input m-b-16" data-validate = "Port is required">
                            <input class="input100" type="text" name="port" placeholder="Port" required>
                            <span class="focus-input100"></span>
                        </div>
                        <div class="container-login100-form-btn m-t-17">
                            <button class="login100-form-btn" type="submit">
                                Add configuration
                            </button>
                        </div>
                    </form>
                    <!-- Qui di seguito si invalida la sessione nel caso ci sia stato un errore nella login -->
                    <%  String errorMessage = (String) session.getAttribute("errorMessage");
                        if (null !=errorMessage) { %>
                    <div class="alert m-t-17">
                        <span class="closebtn">&times;</span>
                        <strong> <%=errorMessage %></strong>
                    </div>
                    <% session.removeAttribute("errorMessage"); %>
                    <%}
                        String address = (String) session.getAttribute("address");
                        if (null !=address) { %>
                    <%  session.removeAttribute("address"); %>
                    <%}
                        String port = (String) session.getAttribute("port");
                        if (null !=port) { %>
                    <%  session.removeAttribute("port"); %>
                    <%}
                    %>
                    <script>
                        const close = document.getElementsByClassName("closebtn");
                        let i;

                        for (i = 0; i < close.length; i++) {
                            close[i].onclick = function(){
                                const div = this.parentElement;
                                div.style.opacity = "0";
                                setTimeout(function(){ div.style.display = "none"; }, 600);
                            }
                        }
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>


<div id="dropDownSelect1"></div>

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


</body>
</html>

