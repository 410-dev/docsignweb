package me.hysong.dev.site.modules.docsign;

import me.hysong.Applications;

public class CentralPathManager {
    public static String getCentralStoragePath() {
        return Applications.getApplicationDataPath("docsign");
    }
}
