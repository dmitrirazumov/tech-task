package org.oddishwolf.api.dao;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oddishwolf.api.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 UserDao tests disabled because data may not exist in the db.
 Class can be refactored to work with mock db.
 */
@Disabled
@Tag("fast")
@Tag("dao_user")
@ExtendWith({MockitoExtension.class})
public class UserDaoTest {

    private static final String AFTER_DROP_TABLES_SQL = "DROP TABLE users; DROP TABLE gender;";
    private static final String AFTER_CLEAN_TABLES_SQL = "TRUNCATE TABLE users, gender;";

    @InjectMocks
    UserDao userDao;

    @Test
    @SneakyThrows
        //first of all delete tables
    void createTablesAndInsertDataTrue() {
        int countOfSuccessInserts = userDao.createTablesAndInsertData();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_DROP_TABLES_SQL);
        }
        assertThat(countOfSuccessInserts).isEqualTo(2);
    }

    @Test
    @SneakyThrows
        //first of all delete tables
    void createTablesTrue() {
        boolean result = userDao.createTables();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_DROP_TABLES_SQL);
        }
        assertThat(result).isTrue();
    }

    @Test
    @SneakyThrows
        //first of clear tables
    void insertInitDataTrue() {
        int countOfSuccessInserts = userDao.insertInitData();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_CLEAN_TABLES_SQL);
        }
        assertThat(countOfSuccessInserts).isEqualTo(2);
    }
}
