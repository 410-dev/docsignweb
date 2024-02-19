package me.hysong.dev.site.modules.docsign.signutil;

import me.hysong.dev.site.modules.Logger;
import me.hysong.dev.site.modules.docsign.*;

import me.hysong.libhycore.CoreHex;
import me.hysong.libhycore.CoreAES;
import me.hysong.libhycore.CoreBase64;
import me.hysong.libhycore.CoreSHA;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReadSignature {
    
    public static Sign readSign(HttpServletRequest request, HttpServletResponse response, String filePath, String mailToVerify) throws Exception {
        Logger.logger(request, response, "Reading signature from file: " + filePath);

        // Read file as hexadecimal
        String hex = CoreHex.readFileToHex(filePath);

        // Encode hex to string
        String str = CoreHex.hexToString(hex);
        String orig = new String(str);

        // Search for header
        if (!str.contains(Sign.HEADER)) {
            Logger.logger(request, response, "Header not found.");
            return null;
        }

        Logger.logger(request, response, "Header found.");


        // Search for footer
        boolean signatureIsAtTheEnd = true;
        if (!str.endsWith(Sign.FOOTER)) {
            signatureIsAtTheEnd = false;
            Logger.logger(request, response, "Warning: Signature is not at the end of the file.");

            // If footer is not at the end of file, signature is invalid
            if (!str.contains(Sign.FOOTER)) {
                Logger.logger(request, response, "Error: Footer not found.");
                return null;
            }
        }

        // Remove header and footer
        str = str.substring(str.indexOf(Sign.HEADER) + Sign.HEADER.length(), str.lastIndexOf(Sign.FOOTER));

        // Decrypt
        str = CoreAES.decrypt(str, mailToVerify);
        Logger.logger(request, response, "SIGN: " + str);

        // Get unsigned hash
        String unsignedHash = orig.substring(0, orig.indexOf(Sign.HEADER));
        unsignedHash = CoreBase64.encode(unsignedHash);
        unsignedHash = CoreSHA.hash512(unsignedHash);
        Logger.logger(request, response, "UNSIGNED HASH: " + unsignedHash);


        // Decode string to sign
        Sign s = Sign.parse(str);
        SignState state = new SignState();
        state.setSignatureValid(signatureIsAtTheEnd);
        state.setUnsignedContentActualSHA(unsignedHash);
        state.setUnsignedContentExpectedSHA(s.getUnsignedHash());
        state.checkValidty(s, mailToVerify);

        s.setSignState(state);
        

        return s;
    }
}
