package org.oddishwolf.api.dao;

import lombok.SneakyThrows;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.util.ConnectionManager;
import org.oddishwolf.api.util.ScriptsReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

public class UserDao implements Dao<String, User> {

    private static final String FIND_ALL_SQL = "SELECT username, first_name, last_name, birthday, email, g.gender_name " +
            "FROM users JOIN gender g ON users.gender = g.id";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE username = ?";
    private static final String FIRST_PART_UPDATE_SQL = "UPDATE users";

    @SneakyThrows
    public int createTablesAndInsertData() {
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

            createStatement.executeUpdate(createSql);
            int insertsCount = insertStatement.executeUpdate(insertSql);

            connection.commit();
            return insertsCount;
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
        return false;
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet dbUsers = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (dbUsers.next()) {
                users.add(buildUser(dbUsers));
            }
            return users;
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
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
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public boolean update(User entity) {
        List<Object> parameters = new ArrayList<>();
        List<String> setSql = new ArrayList<>();

        //build query
        if (entity.getAdditionalParameter() != null) {
            setSql.add("username = ?");
            parameters.add(entity.getAdditionalParameter());
        }
        if (entity.getFirstName() != null) {
            setSql.add("first_name = ?");
            parameters.add(entity.getFirstName());
        }
        if (entity.getLastName() != null) {
            setSql.add("last_name = ?");
            parameters.add(entity.getLastName());
        }
        if (entity.getBirthday() != null) {
            setSql.add("birthday = ?");
            parameters.add(entity.getBirthday());
        }
        if (entity.getEmail() != null) {
            setSql.add("email = ?");
            parameters.add(entity.getEmail());
        }
        if (entity.getGender() != null) {
            setSql.add("gender = ?");
            parameters.add(entity.getGender().ordinal() + 1);
        }
        parameters.add(entity.getUsername());

        String set = setSql.stream()
                .collect(joining(", ", " SET ", " WHERE username = ? "));
        String sql = FIRST_PART_UPDATE_SQL + set;

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    public boolean createTables() {
        String sql = ScriptsReader.readScript("create_tables_script.sql");
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            return !statement.execute(sql);
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    public int insertInitData() {
        String sql = ScriptsReader.readScript("insert_data_script.sql");
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            return statement.executeUpdate(sql);
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
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
}
