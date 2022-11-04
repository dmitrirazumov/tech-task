package org.oddishwolf.api.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.entity.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {

    private static final UserService INSTANCE = new UserService();

    private final UserDao userDao = UserDao.getInstance();

    public Optional<User> get(String username) {
        return userDao.findById(username);
    }

    public List<User> getAll() {
        return userDao.findAll();
    }

    public List<User> filterByAge() {
        List<User> users = userDao.findAll();
        return users.stream().
                filter(user -> getAge(user) < 20)
                .collect(Collectors.toList());
    }

    public List<User> filterByLastNamePostfix() {
        List<User> users = userDao.findAll();
        return users.stream()
                .filter(user -> user.getLastName().endsWith("ов"))
                .collect(Collectors.toList());
    }

    public long getAge(User user) {
        return ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now());
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
