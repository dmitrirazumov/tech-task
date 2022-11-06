package org.oddishwolf.api;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.mapper.UpdateUserMapper;
import org.oddishwolf.api.service.UserService;
import org.oddishwolf.api.validator.UpdateUserValidator;
import org.oddishwolf.api.validator.ValidationResult;

import java.util.Optional;

public class App {
    public static void main(String[] args) {

//        new UserService(new UserDao(), new UpdateUserValidator(), new ValidationResult(), new UpdateUserMapper()).init();
        UserService userService = new UserService(new UserDao(), new UpdateUserValidator(new UserService(new UserDao())), new ValidationResult(), new UpdateUserMapper());
        userService.update(UpdateUserDto.builder().username("username1").birthday("1-13-nope").build());
        Optional<User> username1 = userService.get("username1");
        username1.ifPresent(System.out::println);
    }
}
