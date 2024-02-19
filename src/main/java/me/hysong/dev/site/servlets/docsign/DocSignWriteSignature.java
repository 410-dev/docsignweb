package me.hysong.dev.site.servlets.docsign;

import me.hysong.libhycore.CoreBase64;
import me.hysong.libhycore.CoreDate;
import me.hysong.libhyextended.utils.StackTraceStringifier;
import me.hysong.dev.site.modules.Logger;
import me.hysong.dev.site.modules.docsign.FileDeletionQueue;
import me.hysong.dev.site.modules.docsign.Identity;
import me.hysong.dev.site.modules.docsign.signutil.WriteSignature;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet(name = "DocSignWriteSignature", value = "/sign")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50, // 50 MB
        fileSizeThreshold = 1024 * 1024) // 1 MB
public class DocSignWriteSignature extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(404);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Write signature
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            // Get file part from the request
            Part filePart = request.getPart("file");
            String fileName = filePart.getSubmittedFileName();

            // Create input stream from file part
            InputStream inputStream = filePart.getInputStream();

            // Specify the path to save the uploaded file
            String uploadPath = "/opt/data/docsign/cache/" + fileName;

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

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String daysValid = request.getParameter("daysValid");
            String mininote = request.getParameter("miniNote");
            mininote = mininote == null ? "" : mininote;

            int daysValidInt;

            try {
                daysValidInt = Integer.parseInt(daysValid);
            } catch (Exception e) {
                daysValidInt = 30;
            }

            String outputFileLocation = request.getServletContext().getRealPath("/");
            outputFileLocation += "files/";

            Identity i = new Identity(name, email);
            String[] data = WriteSignature.writeSignature(uploadPath, outputFileLocation, daysValidInt, mininote, i);
            outputFileLocation = data[0];
            String signDat = data[1];

            // Remove the uploaded file
            File file = new File(uploadPath);
            file.delete();

            // Add deletion queue, for next 12 hours
            FileDeletionQueue.addFileToQueue(outputFileLocation, CoreDate.mSecSince1970(1000 * 60 * 60 * 12));

            // From outputFileLocation, remove the webapp path
            outputFileLocation = outputFileLocation.replace(request.getServletContext().getRealPath("/"), "");

            outputFileLocation = CoreBase64.encode(outputFileLocation);

            request.getSession().setAttribute("apps.docsign.signdat", signDat);
            response.sendRedirect(request.getContextPath() + "/sign-done.jsp?file=" + outputFileLocation);
        }catch (Exception e) {
            e.printStackTrace();
            Logger.errorLog(request, response, e.getMessage(), e);
            request.getSession().setAttribute("site.stacktrace", StackTraceStringifier.stringify(e));
            response.sendRedirect(request.getContextPath() + "/error.jsp?errorCode=500");
        }
    }
}
