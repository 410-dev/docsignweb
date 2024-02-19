<%@ page import="me.hysong.dev.site.modules.Logger" %><%--
  Created by IntelliJ IDEA.
  User: hoyounsong
  Date: 2023-04-10
  Time: 3:22 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>DocSign II Web - Verify Page</title>
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
        .form {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border: 2px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            text-align: left;
        }
        label {
            display: block;
            margin-bottom: 10px;
            font-weight: bold;
        }
        input[type="text"], input[type="email"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            margin-bottom: 20px;
            box-sizing: border-box;
        }
        input[type="file"] {
            display: none;
        }
        .upload-button {
            display: inline-block;
            width: 200px;
            height: 50px;
            margin-bottom: 20px;
            text-align: center;
            line-height: 50px;
            font-size: 18px;
            font-weight: bold;
            color: #fff;

            background-color: #2196f3;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            transition: background-color 0.2s ease;
            cursor: pointer;
        }
        .upload-button:hover {
            background-color: #0c7cd5;
        }
        .button {
            display: block;
            width: 200px;
            height: 50px;
            margin: 20px auto;
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
    </style>
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    Logger.accessLogger(request, response);
%>

<h1>Verify Page</h1>
<div class="container">
    <form class="form" action="${pageContext.request.contextPath}/verify" method="post" enctype="multipart/form-data">
        <label for="file-upload">Upload Signed Document:</label>
        <input type="file" id="file-upload" name="file" onchange="document.getElementById('file-name').innerHTML = this.value.split('\\').pop();">
        <label for="file-upload" class="upload-button">Choose File</label>
        <span id="file-name" style="margin-left: 10px;"></span>
        <label for="email">Signer's Email:</label>
        <input type="email" id="email" name="email" required placeholder="aaa@example.com">
        <label for="signature-data">Signature Data (Optional):</label>
        <input type="text" id="signature-data" name="sign">
        <input type="submit" class="button" value="Verify Document">
<%--        <input type="hidden" name="timezone" value="">--%>
    </form>
    <a href="<%=request.getContextPath()%>/" class="button">Back to main</a>
</div>
</body>
</html>

