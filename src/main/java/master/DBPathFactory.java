package master;

import me.hysong.libhycore.CoreLogger;
import me.hysong.dev.site.modules.Logger;

public class DBPathFactory {

    public static final short DB_ME_HYSONG_DEV = 0;
    public static final short DB_CODENAME_ETERNITY = 1;
    public static final short DB_ZMBG_YUPSA_WORLDCUP = 2;

    private static final String LINUX_PATH = "/opt/data/databases/production/";

    private static final String PARENT_NAME_ME_HYSONG_DEV = "me.hysong.dev";
    private static final String PARENT_NAME_CODENAME_ETERNITY = "codename-eternity";
    private static final String PARENT_NAME_ZMBG_YUPSA_WORLDCUP = "codename-eternity";

    private static String getPathOfLinux(String parent, String fileName){
        if (fileName.length() == 0) return LINUX_PATH + parent;
        return LINUX_PATH + parent + "/" + fileName + ".db";
    }

    public static String getPath(short selection, String actualDBName) {
        switch (selection) {
            case DB_ME_HYSONG_DEV:
                return getPathOfLinux(PARENT_NAME_ME_HYSONG_DEV, actualDBName);

            case DB_CODENAME_ETERNITY:
                return getPathOfLinux(PARENT_NAME_CODENAME_ETERNITY, actualDBName);

            case DB_ZMBG_YUPSA_WORLDCUP:
                return getPathOfLinux(PARENT_NAME_ZMBG_YUPSA_WORLDCUP, actualDBName);

            default:
                throw new RuntimeException("Invalid selection: " + selection);
        }
    }

}
