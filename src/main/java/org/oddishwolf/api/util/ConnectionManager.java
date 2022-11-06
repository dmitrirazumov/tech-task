package org.oddishwolf.api.util;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
One thread connection.
Class can be refactored to use connection pool.
*/
@UtilityClass
public class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    static {
        loadDriver();
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException exc) {
            throw new RuntimeException("No connection with db: " + exc);

        }
    }

//    public static void main(String[] args) {
//        try (Connection connection = ConnectionManager.open();
//             Statement statement = connection.createStatement()) {
//            statement.execute("DROP TABLE users; DROP TABLE gender;");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }

    //compatibility with older java versions
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
