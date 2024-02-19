<%@ page import="me.hysong.dev.site.modules.Logger" %><%--
  Created by IntelliJ IDEA.
  User: hoyounsong
  Date: 2023-04-10
  Time: 2:15 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>DocSign II Web - Sign Page</title>
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
            background-color: #2196f3;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            transition: background-color 0.2s ease;
            text-decoration: none;
        }
        .button:hover {
            background-color: #0c7cd5;
        }
        label {
            display: block;
            margin: 10px 0;
            font-size: 18px;
            font-weight: bold;
        }
        input[type="text"] {
            width: 100%;
            padding-top: 5px;
            padding-bottom: 5px;
            border: 2px solid #ccc;
            border-radius: 10px;
            font-size: 18px;
            text-align: center;
        }
        input[type="email"] {
            width: 100%;
            text-align: center;
            padding-top: 5px;
            padding-bottom: 5px;
            border: 2px solid #ccc;
            border-radius: 10px;
            font-size: 18px;
        }
        input[type="number"] {
            width: 100%;
            text-align: center;
            padding-top: 5px;
            padding-bottom: 5px;
            border: 2px solid #ccc;
            border-radius: 10px;
            font-size: 18px;
        }
        input[type="file"] {
            display: none;
        }
        .upload-button {
            display: inline-block;
            padding: 10px 20px;
            margin: 10px 0;
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
        .file-name {
            display: inline-block;
            margin-left: 10px;
            font-size: 18px;
        }
        form {
            display: inline-block;
            width: 500px;
            padding: 20px;
            border: 2px solid #ccc;
            border-radius: 10px;
            background-color: #fff;
        }
        .popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 400px;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            border-radius: 10px;
            z-index: 999;
        }
        .popup h2 {
            font-size: 24px;
            margin-top: 0;
        }
        .popup p {
            font-size: 18px;
            margin-bottom: 20px;
        }
        .popup button {
            display: block;
            width: 200px;
            height: 50px;
            margin: 0 auto;
            text-align: center;
            line-height: 50px;
            font-size: 18px;
            font-weight: bold;
            color: #fff;

            background-color: #2196f3;
            borderborder-radius: 10px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            transition: background-color 0.2s ease;
            cursor: pointer;
        }
        .popup button:hover {
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
<h1>DocSign II Web - Sign Page</h1>
<div class="container">
    <form action="<%=request.getContextPath()%>/sign" method="post" enctype="multipart/form-data">
        <label for="file-upload">Upload File:</label>
        <input type="file" id="file-upload" name="file" max="134217728" onchange="document.getElementById('file-name').textContent = this.files[0].name">
        <label for="file-upload" class="upload-button">Choose File</label>
        <span id="file-name" class="file-name"></span>
        <label for="email">Signer Email:</label>
        <input type="email" id="email" name="email" required placeholder="aaa@example.com">
        <label for="name">Signer Name:</label>
        <input type="text" id="name" name="name" required>
        <label for="days-valid">Days Valid:</label>
        <input type="number" id="days-valid" name="daysValid" required value="30">
        <label for="mini-note">Mini Note:</label>
        <input type="text" id="mini-note" name="miniNote">
        <button type="submit" class="button">Sign Document</button>
    </form>
    <button class="button" onclick="showPopup()">Disclaimer</button>
    <a href="<%=request.getContextPath()%>/" class="button">Back to main</a>
    <div id="popup" class="popup">
        <h2>Disclaimer</h2>
        <p>This is a disclaimer for the DocSign II Web application. Please read it carefully before using the application.</p>
        <p>Disclaimer 1: Please be aware that by inserting signature data directly into your document, there may be instances where the document cannot be read unless the signature is removed. It is important to note that this verification applies to PDF, PNG, and JPG file formats only.
            <br><br>
            Disclaimer 2: This document protection method serves to prevent modifications to the original document. However, it should be noted that this protection does not guarantee the identification of a fake document. As a user, it is your responsibility to exercise caution and verify the authenticity of any documents you encounter.</p>
        <button class="button" onclick="hidePopup()">Close</button>
    </div>
</div>
<script>
    function showPopup() {
        document.getElementById("popup").style.display = "block";
    }
    function hidePopup() {
        document.getElementById("popup").style.display = "none";
    }
</script>
</div>
</body>
</html>