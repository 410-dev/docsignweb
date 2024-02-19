package me.hysong.dev.site.modules;

import master.DBPathFactory;
import me.hysong.libhyextended.sql.SQLConnection;

public class SQLiteFactory {

    private static SQLConnection builder() {
        SQLConnection connection = new SQLConnection();
        connection.setUseSQLite(true);
        connection.setSecondsTimeout(1);
        connection.setUseAutoClose(true);
        connection.setUsername("local");
        connection.setPassword("local");
        return connection;
    }

    public static SQLConnection getURLShortenerDatabase() {
        SQLConnection connection = builder();
        connection.setAddress(DBPathFactory.getPath(DBPathFactory.DB_ME_HYSONG_DEV, "links"));
        return connection;
    }

    public static SQLConnection getLogDatabase() {
        SQLConnection connection = builder();
        connection.setAddress(DBPathFactory.getPath(DBPathFactory.DB_ME_HYSONG_DEV, "logs"));
        return connection;
    }

    public static SQLConnection getImageSelectionFactory() {
        SQLConnection connection = builder();
        connection.setAddress(DBPathFactory.getPath(DBPathFactory.DB_ZMBG_YUPSA_WORLDCUP, "image-selection"));
        return connection;
    }
}
