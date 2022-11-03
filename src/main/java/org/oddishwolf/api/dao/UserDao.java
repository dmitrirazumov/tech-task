package org.oddishwolf.api.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements Dao<String, User> {

    private static final UserDao INSTANCE = new UserDao();

    private static final String FIND_ALL_SQL = "SELECT username, first_name, last_name, birthday, email, g.gender_name " +
            "FROM users JOIN gender g on users.gender = g.id;";

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet dbUsers = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (dbUsers.next()) {
                users.add(User.builder()
                        .username(dbUsers.getString("username"))
                        .firstName(dbUsers.getString("first_name"))
                        .lastName(dbUsers.getString("last_name"))
                        .birthday(dbUsers.getDate("birthday").toLocalDate())
                        .email(dbUsers.getString("email"))
                        .gender(Gender.valueOf(dbUsers.getString("gender_name")))
                        .build());
            }
            return users;
        }
    }

    @Override
    public Optional<User> findById(String key) {
        return Optional.empty();
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
