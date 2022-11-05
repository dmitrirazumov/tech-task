package org.oddishwolf.api.service;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.exception.ValidationException;
import org.oddishwolf.api.mapper.UpdateUserMapper;
import org.oddishwolf.api.validator.UpdateUserValidator;
import org.oddishwolf.api.validator.ValidationResult;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

public class UserService {

    private final UserDao userDao;
    private UpdateUserValidator updateUserValidator;
    private ValidationResult validationResult;
    private UpdateUserMapper updateUserMapper;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserService(UserDao userDao, UpdateUserValidator updateUserValidator, ValidationResult validationResult, UpdateUserMapper updateUserMapper) {
        this.userDao = userDao;
        this.updateUserValidator = updateUserValidator;
        this.validationResult = validationResult;
        this.updateUserMapper = updateUserMapper;
    }

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
                .collect(toList());
    }

    //return users whose last name ends with "ов"
    public List<User> filterByLastNamePostfix() {
        List<User> users = userDao.findAll();
        return users.stream()
                .filter(user -> user.getLastName().endsWith("ов"))
                .collect(toList());
    }

    //updating any field of user
    public boolean update(UpdateUserDto userDto) {
        //validation
        validationResult = updateUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getExceptions());
        }
        //mapping
        User userEntity = updateUserMapper.mapFrom(userDto);
        //update
        userDao.update(userEntity);

        return true;
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
}
