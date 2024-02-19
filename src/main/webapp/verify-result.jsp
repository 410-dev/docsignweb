<%@ page import="me.hysong.dev.site.servlets.docsign.DocSignVerifySignature" %>
<%@ page import="me.hysong.dev.site.modules.docsign.SignatureReport" %>
<%@ page import="me.hysong.dev.site.modules.Logger" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.DateFormat" %><%--
  Created by IntelliJ IDEA.
  User: hoyounsong
  Date: 2023-04-10
  Time: 4:03 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>DocSign II Web - Signing Result</title>
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
        .table {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border: 2px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            text-align: left;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ccc;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        .note {
            width: 100%;
            height: 100px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            margin-bottom: 20px;
            box-sizing: border-box;
            resize: none;
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
            background-color: #2196f3;
            text-decoration: none;
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
<%
    Object o = request.getSession().getAttribute("app.docsign.report");
    if (o == null) {
        response.sendError(404);
        return;
    }

    SignatureReport report = (SignatureReport) o;

    Date signedTimeFormat = new Date(report.getSignedAt() * 1000L);
    Date validThroughTimeFormat = new Date(report.getValidThrough() * 1000L);
    Date currentTimeFormat = new Date();

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    format.setTimeZone(TimeZone.getTimeZone("UTC"));


    String formattedTimeSigned = format.format(signedTimeFormat) + " UTC+00:00";
    String formattedTimeValidThrough = format.format(validThroughTimeFormat) + " UTC+00:00";
    String currentTime = format.format(currentTimeFormat) + " UTC+00:00";

    String message = request.getParameter("decf") == null ? "" : "It seems that the email address you entered is not valid, or does not have digital signature.";
%>
<h1>Signing Result</h1>
<div class="container">
    <div class="table">
        <h2>Signature Details</h2>
        <p style="color: red"><%=message%></p>
        <table>
            <thead>
                <tr>
                    <th>Signer Version</th>
                    <th>Signer Name</th>
                    <th>Signer Email</th>
                    <th>Currently Valid</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><%=report.getSignVersion()%></td>
                    <td><%=report.getSignerName()%></td>
                    <td><%=report.getSignerEmail()%></td>
                    <td><%=report.getCurrentlyValid()%></td><br>
                </tr>
            </tbody>
        </table>

        <table>
            <thead>
            <tr>
                <th>Signed Date</th>
                <th>Signature Valid Date</th>
                <th>Verification Request Date</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><%=formattedTimeSigned%></td>
                <td><%=formattedTimeValidThrough%></td>
                <td><%=currentTime%></td><br>
            </tr>
            </tbody>
        </table>


        <table>
            <thead>
            <tr>
                <th>Mini Note</th>
                <th>Unsigned Hash</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><textarea readonly onclick="this.select()" class="note"><%=report.getMiniNote()%></textarea></td>
                <td><textarea readonly onclick="this.select()" class="note"><%=report.getUnsignedContentExpectedSHA()%></textarea></td>
            </tr>
            </tbody>
        </table>
        <h2>Verification Details</h2>
        <table>
            <thead>
            <tr>
                <th>Sign Version</th>
                <th>Mail Matches</th>
                <th>Time Valid</th>
                <th>Unsigned Content</th>
                <th>Unique Pair</th>
                <th>Result</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><%=report.getSignedVersionCompatibility()%></td>
                <td><%=report.getMailMatches()%></td>
                <td><%=report.getTimeValid()%></td>
                <td><%=report.getUnsignedMatches()%></td>
                <td><%=report.getUniquePair()%></td>
                <td><%=report.getValidity()%></td>
            </tr>
            </tbody>
        </table>
        <p>Your file is not stored in the server after verification is completed.</p>
        <a href="<%=request.getContextPath()%>/" class="button">Back to main</a>
    </div>
</div>
</body>
</html>
