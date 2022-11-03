package org.oddishwolf.api.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;

@UtilityClass
public class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    static {
        loadDriver();
    }

    @SneakyThrows
    //one thread connection
    public static Connection open() {
        return DriverManager.getConnection(
                PropertiesUtil.get(URL_KEY),
                PropertiesUtil.get(USERNAME_KEY),
                PropertiesUtil.get(PASSWORD_KEY)
        );
    }

    //compatibility with older java versions
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
