<%@ page import="me.hysong.dev.site.modules.Logger" %>
<%@ page import="me.hysong.dev.site.modules.docsign.FileDeletionQueue" %><%--
  Created by IntelliJ IDEA.
  User: hoyounsong
  Date: 2023-04-10
  Time: 2:01 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>DocSign II Web</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
        }
        .button {
            display: block;
            width: 200px;
            height: 50px;
            margin: 10px auto;
            text-align: center;
            line-height: 50px;
            font-size: 18px;
            font-weight: bold;
            color: #fff;
            text-decoration: none;
            background-color: #2196f3;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            transition: background-color 0.2s ease;
        }
        .button:hover {
            background-color: #0c7cd5;
        }
        h1 {
            text-align: center;
            font-size: 36px;
            margin-top: 50px;
        }
        .container {
            margin-top: 50px;
            text-align: center;
        }
    </style>
</head>
<%
    Logger.accessLogger(request, response);
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    FileDeletionQueue.createWorkerThread();
%>
<body>
    <h1>DocSign II Web</h1>
    <div class="container">
        <a href="sign.jsp" class="button">Sign</a>
        <a href="verify.jsp" class="button">Verify</a>
    </div>
</body>
</html>
