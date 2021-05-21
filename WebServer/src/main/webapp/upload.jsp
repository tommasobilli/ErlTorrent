<%--
  Created by IntelliJ IDEA.
  User: andreadidonato
  Date: 21/05/21
  Time: 17:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<%
    String uname = (String) session.getAttribute("username");
    if (null == uname) {
        session.setAttribute("errorMessage", "Please login first");
        response.sendRedirect("index.jsp");
    }
%>

<body>

</body>
</html>
