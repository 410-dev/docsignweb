<%@ page import="me.hysong.libhycore.CoreBase64" %>
<%@ page import="me.hysong.dev.site.modules.Logger" %><%--
  Created by IntelliJ IDEA.
  User: hoyounsong
  Date: 2023-04-10
  Time: 3:03 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>DocSign II Web - Signing Done</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
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
        .result {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border: 2px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            text-align: left;
            white-space: pre-wrap;
            word-break: break-all;
        }
        .button {
            display: block;
            width: 40%;
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
            cursor: pointer;
        }
        .button:hover {
            background-color: #0c7cd5;
        }
        .download-link {
            margin-top: 20px;
            font-size: 18px;
        }
        .download-link a {
            color: #2196f3;
            text-decoration: none;
        }
        .download-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h1>Signing Done</h1>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    Logger.accessLogger(request, response);
%>
<%
    if (request.getParameter("file") == null) response.sendError(404);
    Object signatureDataRAW = session.getAttribute("apps.docsign.signdat");
    if (signatureDataRAW == null) response.sendError(404);

    String signatureData = (String) signatureDataRAW;
    String filePath = request.getContextPath() + "/" + CoreBase64.decode(request.getParameter("file"));

    String currentParentURL = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "");
    if (currentParentURL.endsWith("/")) currentParentURL = currentParentURL.substring(0, currentParentURL.length() - 1);


    // Remove session data
//    session.removeAttribute("apps.docsign.signdat");
%>

<div class="container">
    <div class="result">
        <p><strong>Your document has been successfully signed!</strong></p>
        <p>You can copy the encrypted signature string below for later verification:</p>
        <textarea readonly onclick="this.select()" style="resize: none; width: 100%; height: 1em;"><%=signatureData%></textarea>

        <p>The signed document has been saved on our server for the next 12 hours for sharing purpose. You can download it using the button below:</p>
        <a href="<%=currentParentURL%><%=filePath%>" class="button" download>Download Signed Document</a>
        <p class="download-link"><em>Or share the download link with others:</em> <a href="<%=currentParentURL%><%=filePath%>"><%=currentParentURL%><%=filePath%></a></p>
        <p><em>Note: The signed document will be deleted from our server after 12 hours for sharing purpose.</em></p>
        <p>Do not refresh this page! You won't see this page again.</p>

        <br>
        <a href="<%=request.getContextPath()%>/" class="button">Back to main</a>
    </div>
</div>
</body>
</html>
