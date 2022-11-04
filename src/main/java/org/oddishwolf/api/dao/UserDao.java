package org.oddishwolf.api.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.util.ConnectionManager;
import org.oddishwolf.api.util.ScriptsReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements Dao<String, User> {

    private static final UserDao INSTANCE = new UserDao();

    private static final String SAVE_SQL = "INSERT INTO users (username, first_name, last_name, birthday, email, gender) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_ALL_SQL = "SELECT username, first_name, last_name, birthday, email, g.gender_name " +
            "FROM users JOIN gender g ON users.gender = g.id";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE username = ?";

    @SneakyThrows
    public boolean createTablesAndInsertData() {
        String createSql = ScriptsReader.readScript("create_tables_script.sql");
        String insertSql = ScriptsReader.readScript("insert_data_script.sql");

        Connection connection = null;
        Statement createStatement = null;
        Statement insertStatement = null;

        try {
            connection = ConnectionManager.open();
            createStatement = connection.createStatement();
            insertStatement = connection.createStatement();

            connection.setAutoCommit(false);

            createStatement.execute(createSql);
            insertStatement.executeUpdate(insertSql);

            connection.commit();
            return true;
        } catch (Exception exc) {
            if (connection != null) {
                connection.rollback();
            }
            throw exc;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (createStatement != null) {
                createStatement.close();
            }
            if (insertStatement != null) {
                insertStatement.close();
            }
        }
    }

    @Override
    public boolean save(User entity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setDate(4, Date.valueOf(entity.getBirthday()));
            preparedStatement.setString(5, entity.getEmail());
            preparedStatement.setInt(6, entity.getGender().ordinal() + 1);

            preparedStatement.executeUpdate();

            ResultSet dbNewUser = preparedStatement.getGeneratedKeys();
            return dbNewUser.next();
        } catch (SQLException exc) {
            return false;
        }
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet dbUsers = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (dbUsers.next()) {
                users.add(buildUser(dbUsers));
            }
            return users;
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(String key) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setString(1, key);
            ResultSet dbUser = preparedStatement.executeQuery();

            User user = null;
            if (dbUser.next()) {
                user = buildUser(dbUser);
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    public boolean createTables() {
        String sql = ScriptsReader.readScript("create_tables_script.sql");
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
            return true;
        } catch (SQLException exc) {
            return false;
        }
    }

    public int insertInitData() {
        String sql = ScriptsReader.readScript("insert_data_script.sql");
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            return statement.executeUpdate(sql);
        } catch (SQLException exc) {
            return 0;
        }
    }

    private User buildUser(ResultSet dbUsers) throws SQLException {
        return User.builder()
                .username(dbUsers.getString("username"))
                .firstName(dbUsers.getString("first_name"))
                .lastName(dbUsers.getString("last_name"))
                .birthday(dbUsers.getDate("birthday").toLocalDate())
                .email(dbUsers.getString("email"))
                .gender(Gender.valueOf(dbUsers.getString("gender_name")))
                .build();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
