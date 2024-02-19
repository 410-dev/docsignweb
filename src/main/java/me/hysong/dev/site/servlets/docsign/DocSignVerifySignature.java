package me.hysong.dev.site.servlets.docsign;

import me.hysong.libhycore.CoreSHA;
import me.hysong.libhyextended.utils.StackTraceStringifier;
import me.hysong.dev.site.modules.Logger;
import me.hysong.dev.site.modules.docsign.Sign;
import me.hysong.dev.site.modules.docsign.SignState;
import me.hysong.dev.site.modules.docsign.SignatureReport;
import me.hysong.dev.site.modules.docsign.signutil.ReadSignature;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@WebServlet(name = "DocSignVerifySignature", value = "/verify")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50, // 50 MB
        fileSizeThreshold = 1024 * 1024) // 1 MB
public class DocSignVerifySignature extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(404);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            // Get file part from the request
            Part filePart = request.getPart("file");
            String fileName = filePart.getSubmittedFileName();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + CoreSHA.hash512(UUID.randomUUID().toString()).substring(0, 4) + fileName.substring(fileName.lastIndexOf("."));

            // Create input stream from file part
            InputStream inputStream = filePart.getInputStream();

            // Specify the path to save the uploaded file
            String uploadPath = "/opt/data/docsign/" + fileName;

            // Create a file output stream to write the uploaded file to disk
            FileOutputStream outputStream = new FileOutputStream(uploadPath);

            // Read the bytes of the uploaded file and write them to the output stream
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();


            String mailToVerify = request.getParameter("email");
            String matchingSignData = request.getParameter("sign");

            SignatureReport report;
            boolean decryptionSuccess = false;
            try {
                Sign s = ReadSignature.readSign(request, response, uploadPath, mailToVerify);
                if (s == null) {
                    Logger.logger(request, response, "Error: Signature is invalid. (s is null)");
                    throw new Exception("Signature is invalid. (s is null)");
                }

                SignState sstate = s.getSignState();

                if (sstate == null) {
                    Logger.logger(request, response, "Error: Signature is invalid. (sstate is null)");
                    throw new Exception("Signature is invalid. (sstate is null)");
                }

                String signVersion = s.getVersion();
                String signerName = s.getName();
                String signerEmail = s.getEmail();
                long signedAt = s.getSignedAt();
                long validThrough = s.getValidthrough();
                String currentlyValid = (System.currentTimeMillis() / 1000L < s.getValidthrough()) ? "Yes" : "No";
                String miniNote = s.getMininote();
                String unsignedContentExpectedSHA = s.getUnsignedHash();
                String signedVersionCompatibility = sstate.getSignedVersion().equals(Sign.VERSION) ? "✅ Compatible" : "⚠️ Warning";
                String mailMatches = sstate.isEmailMatches() ? "✅ Yes" : "❌ Invalid";
                String timeValid = sstate.isDateValid() ? "✅ Valid" : "❌ Invalid";
                String unsignedMatches = sstate.isUnsignedHashMatches() ? "✅ Hash Match" : "❌ Invalid";
                String signMatch = s.getSignHashed();
                String signMatched;
                if (matchingSignData != null && !matchingSignData.isEmpty()) {
                    Logger.logger(request, response, "Unique Pair Sign Data: " + matchingSignData);
                    signMatched = s.getSignHashed().equals(matchingSignData) ? "✅ Hash Match" : "❌ Invalid";
                } else {
                    signMatched = "⚠️ Not provided";
                }
                String validity = sstate.isDateValid() && sstate.isEmailMatches() && sstate.isUnsignedHashMatches() ? "✅ Signature Valid" : "❌ Signature Invalid";
                if (matchingSignData != null && !matchingSignData.isEmpty()) {
                    validity = sstate.isDateValid() && sstate.isEmailMatches() && sstate.isUnsignedHashMatches() && signMatch.equals(matchingSignData) ? "✅ Signature Valid" : "❌ Signature Invalid";
                }

                report = new SignatureReport(signVersion, signerName, signerEmail, signedAt, validThrough, currentlyValid, miniNote, unsignedContentExpectedSHA, signedVersionCompatibility, mailMatches, timeValid, unsignedMatches, validity, signMatch, signMatched);
                decryptionSuccess = true;
            }catch (Exception e) {
                String signVersion = "-";
                String signerName = "-";
                String signerEmail = "-";
                long signedAt = 0;
                long validThrough = 0;
                String currentlyValid = "No";
                String miniNote = "-";
                String unsignedContentExpectedSHA = "-";
                String signedVersionCompatibility = "❌ Invalid";
                String mailMatches = "❌ Invalid";
                String timeValid = "❌ Invalid";
                String unsignedMatches = "❌ Invalid";
                String signMatch = "-";
                String signMatched = "❌ Invalid";
                String validity = "❌ Signature Invalid";

                report = new SignatureReport(signVersion, signerName, signerEmail, signedAt, validThrough, currentlyValid, miniNote, unsignedContentExpectedSHA, signedVersionCompatibility, mailMatches, timeValid, unsignedMatches, validity, signMatch, signMatched);
            }

            File deleteFile = new File(uploadPath);
            deleteFile.delete();


            request.getSession().setAttribute("app.docsign.report", report);
            response.sendRedirect(request.getContextPath() + "/verify-result.jsp" + (decryptionSuccess ? "" : "?decf=1"));
        }catch (Exception e) {
            e.printStackTrace();
            Logger.errorLog(request, response, e.getMessage(), e);
            request.getSession().setAttribute("site.stacktrace", StackTraceStringifier.stringify(e));
            response.sendRedirect(request.getContextPath() + "/error.jsp?errorCode=500");
        }
    }
}
