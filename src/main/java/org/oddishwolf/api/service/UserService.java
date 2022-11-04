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

    //return users which younger than 20
    public List<User> filterByAge() {
        List<User> users = userDao.findAll();
        return users.stream().
                filter(user -> getAge(user) < 20)
                .collect(Collectors.toList());
    }

    //return users whose last name ends with "ов"
    public List<User> filterByLastNamePostfix() {
        List<User> users = userDao.findAll();
        return users.stream()
                .filter(user -> user.getLastName().endsWith("ов"))
                .collect(Collectors.toList());
    }

    //create tables for db and insert test data
    public boolean init() {
        return userDao.createTablesAndInsertData();
    }

    //create tables for db without test data
    public boolean initTables() {
        return userDao.createTables();
    }

    //insert test data if tables already created
    public int initData() {
        return userDao.insertInitData();
    }

    public long getAge(User user) {
        return ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now());
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
