package me.hysong.dev.site.modules.docsign.signutil;

import me.hysong.dev.site.modules.Logger;
import me.hysong.dev.site.modules.docsign.*;

import me.hysong.libhycore.CoreHex;
import me.hysong.libhycore.CoreBase64;
import me.hysong.libhycore.CoreSHA;

import java.util.UUID;

public class WriteSignature {
    public static String[] writeSignature(String document, String saveDir, int daysValid, String mininote, Identity identity) throws Exception {
        Sign s = new Sign(identity, daysValid);
        s.setSignState(new SignState());
        s.setMininote(mininote);

        // Check if file has signature
        String fileHex = CoreHex.readFileToHex(document);
        String str = CoreHex.hexToString(fileHex);

        // Search for header
        if (str.contains(Sign.HEADER)) {
            Logger.simpleLog("Header found.");
            // Remove header and footer
            str = str.substring(0, str.lastIndexOf(Sign.HEADER));
            fileHex = CoreHex.stringToHex(str);
        }

        // Write signature to file

        String unsignedContent = CoreBase64.encode(CoreHex.hexToString(fileHex));
        unsignedContent = CoreSHA.hash512(unsignedContent);
        s.setUnsignedHash(unsignedContent);
        String signatureHex = CoreHex.stringToHex(s.toString());
        String newHex = fileHex + signatureHex;

        // Get file extension
        String ext = document.substring(document.lastIndexOf(".") + 1);
//        String parentDirectory = document.substring(0, document.lastIndexOf("/"));
        String fileNameWithoutExt = document.substring(document.lastIndexOf("/") + 1, document.lastIndexOf("."));
        String fingerprint = CoreSHA.hash512(UUID.randomUUID().toString()).substring(0, 5);
        ext = ".signed-" + fingerprint + "." + ext;

        // Write hex to file
        Logger.simpleLog("Writing signature to file: " + saveDir + fileNameWithoutExt + ext);
        CoreHex.writeHexToFile(saveDir + fileNameWithoutExt + ext, newHex);
        return new String[]{saveDir + fileNameWithoutExt + ext, s.getSignHashed()};
    }
}
