package org.oddishwolf.api.dao;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Disabled because uses embedded H2.
 * Before execute change properties and be sure yours test db is clean.
 */
//@Disabled
@Tag("fast")
@Tag("dao_user")
@ExtendWith({MockitoExtension.class})
public class UserDaoTest {

    private static final String AFTER_DROP_TABLES_SQL = "DROP TABLE users; DROP TABLE gender;";
    private static final String AFTER_CLEAN_TABLES_SQL = "TRUNCATE TABLE users; TRUNCATE TABLE users;";
    private static final List<User> FAKE_USERS_TO_FIND = List.of(
            User.builder().username("username1").firstName("first_name1").lastName("Васильков").birthday(LocalDate.of(2022, 1, 1)).email("email1@java.com").gender(Gender.MALE).build(),
            User.builder().username("username2").firstName("first_name2").lastName("dummy").birthday(LocalDate.of(2022, 1, 2)).email("email2@java.com").gender(Gender.FEMALE).build(),
            User.builder().username("username3").firstName("first_name3").lastName("dummy").birthday(LocalDate.of(2022, 1, 3)).email("email3@java.com").gender(Gender.MALE).build(),
            User.builder().username("username4").firstName("first_name4").lastName("Иванов").birthday(LocalDate.of(2022, 1, 4)).email("email4@java.com").gender(Gender.FEMALE).build(),
            User.builder().username("username5").firstName("first_name5").lastName("dummy").birthday(LocalDate.of(2022, 1, 5)).email("email5@java.com").gender(Gender.MALE).build(),
            User.builder().username("username6").firstName("first_name6").lastName("dummy").birthday(LocalDate.of(2022, 1, 6)).email("email6@java.com").gender(Gender.FEMALE).build(),
            User.builder().username("username7").firstName("first_name7").lastName("Антонов").birthday(LocalDate.of(2022, 1, 7)).email("email7@java.com").gender(Gender.MALE).build(),
            User.builder().username("username8").firstName("first_name8").lastName("dummy").birthday(LocalDate.of(2022, 1, 8)).email("email8@java.com").gender(Gender.FEMALE).build(),
            User.builder().username("username9").firstName("first_name9").lastName("dummy").birthday(LocalDate.of(2022, 1, 9)).email("email9@java.com").gender(Gender.MALE).build(),
            User.builder().username("username10").firstName("first_name10").lastName("Датаджайлов").birthday(LocalDate.of(2022, 1, 10)).email("email10@java.com").gender(Gender.FEMALE).build()
    );
    private static final List<User> FAKE_USERS_TO_UPDATE = List.of(
            //for correct update
            User.builder().username("username1").firstName("up_fn1").build(),
            User.builder().username("username2").firstName("up_fn2").lastName("up_ln2").build(),
            User.builder().username("username3").firstName("up_fn3").lastName("up_ln3").birthday(LocalDate.of(2003, 3, 3)).build(),
            User.builder().username("username4").firstName("up_fn4").lastName("up_ln4").birthday(LocalDate.of(2004, 4, 3)).email("up_e4@java.com").build(),
            User.builder().username("username5").firstName("up_fn5").lastName("up_ln5").birthday(LocalDate.of(2005, 5, 3)).email("up_e5@java.com").gender(Gender.FEMALE).build(),
            User.builder().username("username6").additionalParameter("up_username6").build(),

            //for incorrect update
            User.builder().username("dummy").firstName("dummy").build(),
            User.builder().username("username1").build()
    );

    @InjectMocks
    UserDao userDao;

    @Test
    @SneakyThrows
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
    void insertInitDataTrue() {
        userDao.createTables();
        int countOfSuccessInserts = userDao.insertInitData();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_CLEAN_TABLES_SQL);
            statement.execute(AFTER_DROP_TABLES_SQL);
        }
        assertThat(countOfSuccessInserts).isEqualTo(2);
    }

    @Test
    @SneakyThrows
    void findAllIsEqualToExpected() {
        userDao.createTablesAndInsertData();
        List<User> maybeUsers = userDao.findAll();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_CLEAN_TABLES_SQL);
            statement.execute(AFTER_DROP_TABLES_SQL);
        }
        assertThat(maybeUsers).hasSize(10);
        assertThat(maybeUsers).isEqualTo(FAKE_USERS_TO_FIND);
    }

    @Test
    @SneakyThrows
    void findAllIsNotEqualToExpected() {
        userDao.createTablesAndInsertData();
        List<User> maybeUsers = userDao.findAll();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_CLEAN_TABLES_SQL);
            statement.execute(AFTER_DROP_TABLES_SQL);
        }
        assertThat(maybeUsers).hasSize(10);
        assertThat(maybeUsers).isNotEqualTo(FAKE_USERS_TO_FIND.get(5));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.dao.UserDaoTest#getArgumentsForFindByIdTestIsEqualToExpected")
    void findByIdIsEqualToExpected(String id, User expectedUser, int ddlDb) {
        if (ddlDb == 1) userDao.createTablesAndInsertData();
        Optional<User> maybeUser = userDao.findById(id);
        assertThat(maybeUser).isPresent();
        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(expectedUser));
        if (ddlDb == 2) {
            try (Connection connection = ConnectionManager.open();
                 Statement statement = connection.createStatement()) {
                statement.execute(AFTER_CLEAN_TABLES_SQL);
                statement.execute(AFTER_DROP_TABLES_SQL);
            }
        }
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.dao.UserDaoTest#getArgumentsForFindByIdTestIsNotEqualToExpected")
    void findByIdIsNotEqualToExpected(String id, int createOrDestroyDb) {
        if (createOrDestroyDb == 1) userDao.createTablesAndInsertData();
        Optional<User> maybeUser = userDao.findById(id);
        assertThat(maybeUser).isNotPresent();
        if (createOrDestroyDb == 2) {
            try (Connection connection = ConnectionManager.open();
                 Statement statement = connection.createStatement()) {
                statement.execute(AFTER_CLEAN_TABLES_SQL);
                statement.execute(AFTER_DROP_TABLES_SQL);
            }
        }
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.dao.UserDaoTest#getArgumentsForUpdateIsEqualToExpected")
    void updateTestIsCorrectNumberOfUpdates(User userToUpdate, int createOrDestroyDb) {
        if (createOrDestroyDb == 1) userDao.createTablesAndInsertData();
        boolean maybeUpdatedUser = userDao.update(userToUpdate);
        assertThat(maybeUpdatedUser).isTrue();
        if (createOrDestroyDb == 2) {
            try (Connection connection = ConnectionManager.open();
                 Statement statement = connection.createStatement()) {
                statement.execute(AFTER_CLEAN_TABLES_SQL);
                statement.execute(AFTER_DROP_TABLES_SQL);
            }
        }
    }

    @SneakyThrows
    @Test
    void updateTestReturnFalseIfUserDoesNotExist() {
        userDao.createTablesAndInsertData();
        boolean maybeUpdatedUser = userDao.update(FAKE_USERS_TO_UPDATE.get(6));
        assertThat(maybeUpdatedUser).isFalse();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_CLEAN_TABLES_SQL);
            statement.execute(AFTER_DROP_TABLES_SQL);
        }
    }

    @SneakyThrows
    @Test
    void updateTestReturnFalseIfUserExistButFieldsToUpdateAreEmpty() {
        userDao.createTablesAndInsertData();
        boolean maybeUpdatedUser = userDao.update(FAKE_USERS_TO_UPDATE.get(6));
        assertThat(maybeUpdatedUser).isFalse();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(AFTER_CLEAN_TABLES_SQL);
            statement.execute(AFTER_DROP_TABLES_SQL);
        }
    }

    static Stream<Arguments> getArgumentsForFindByIdTestIsEqualToExpected() {
        return Stream.of(
                Arguments.of(FAKE_USERS_TO_FIND.get(0).getUsername(), FAKE_USERS_TO_FIND.get(0), 1),
                Arguments.of(FAKE_USERS_TO_FIND.get(1).getUsername(), FAKE_USERS_TO_FIND.get(1), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(2).getUsername(), FAKE_USERS_TO_FIND.get(2), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(3).getUsername(), FAKE_USERS_TO_FIND.get(3), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(4).getUsername(), FAKE_USERS_TO_FIND.get(4), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(5).getUsername(), FAKE_USERS_TO_FIND.get(5), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(6).getUsername(), FAKE_USERS_TO_FIND.get(6), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(7).getUsername(), FAKE_USERS_TO_FIND.get(7), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(8).getUsername(), FAKE_USERS_TO_FIND.get(8), 0),
                Arguments.of(FAKE_USERS_TO_FIND.get(9).getUsername(), FAKE_USERS_TO_FIND.get(9), 2)
        );
    }

    static Stream<Arguments> getArgumentsForFindByIdTestIsNotEqualToExpected() {
        return Stream.of(
                Arguments.of("dummy1", 1),
                Arguments.of("dummy2", 0),
                Arguments.of("dummy3", 0),
                Arguments.of("dummy4", 0),
                Arguments.of("dummy5", 0),
                Arguments.of("dummy6", 0),
                Arguments.of("dummy7", 0),
                Arguments.of("dummy8", 0),
                Arguments.of("dummy9", 0),
                Arguments.of("dummy10", 2)
        );
    }

    static Stream<Arguments> getArgumentsForUpdateIsEqualToExpected() {
        return Stream.of(
                Arguments.of(FAKE_USERS_TO_UPDATE.get(0), 1),
                Arguments.of(FAKE_USERS_TO_UPDATE.get(1), 0),
                Arguments.of(FAKE_USERS_TO_UPDATE.get(2), 0),
                Arguments.of(FAKE_USERS_TO_UPDATE.get(3), 0),
                Arguments.of(FAKE_USERS_TO_UPDATE.get(4), 0),
                Arguments.of(FAKE_USERS_TO_UPDATE.get(5), 2)
        );
    }
}
