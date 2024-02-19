<%@ page import="me.hysong.dev.site.modules.Logger" %><%--
  Created by IntelliJ IDEA.
  User: hoyounsong
  Date: 2023-04-10
  Time: 10:45 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Error</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #F5F5F5;
    }
    h1 {
      color: #333333;
    }
    p {
      color: #666666;
      font-size: 16px;
    }
    hr {
      border: 0;
      height: 1px;
      background-color: #CCCCCC;
    }
    textarea {
      width: 100%;
      padding: 10px;
      border-radius: 5px;
      border: 1px solid #CCCCCC;
      font-size: 14px;
    }
    input[type="submit"] {
      background-color: #428bca;
      border: none;
      color: #FFFFFF;
      padding: 10px 20px;
      text-align: center;
      text-decoration: none;
      display: inline-block;
      font-size: 16px;
      border-radius: 5px;
      cursor: pointer;
    }
    input[type="submit"]:hover {
      background-color: #3071a9;
    }
  </style>
</head>
<%
  request.setCharacterEncoding("UTF-8");
  response.setCharacterEncoding("UTF-8");
  Logger.accessLogger(request, response);
%>
<%

    String errorCode = request.getParameter("errorCode");
    Object stackTrace = session.getAttribute("site.stacktrace");

    if (stackTrace == null) {
      Logger.logger(request, response, "Error page accessed without stack trace");
//      response.sendError(404);
      return;
    }else{
      session.removeAttribute("site.stacktrace");
    }
%>
<body>
<div style="margin: 20px auto; max-width: 800px; background-color: #FFFFFF; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);">
  <h1>Error</h1>
  <p>An error occurred while processing your request.</p>
  <p>Error code: <%=errorCode%></p>
  <p>Please try again later or contact support if the problem persists.</p>
  <hr>
  <h2>Stack Trace:</h2>
  <textarea rows="10" cols="80" style="resize: none"><%=stackTrace.toString()%></textarea>
  <br><br>
  <form action="index.jsp">
    <input type="submit" value="Back to Main">
  </form>
</div>
</body>
</html>
