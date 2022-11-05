package org.oddishwolf.api;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.mapper.UpdateUserMapper;
import org.oddishwolf.api.service.UserService;
import org.oddishwolf.api.validator.UpdateUserValidator;
import org.oddishwolf.api.validator.ValidationResult;

import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {

        UserService userService = new UserService(new UserDao(), new UpdateUserValidator(), new ValidationResult(), new UpdateUserMapper());
        Optional<User> username1 = userService.get("username1");
        System.out.println(username1);

        List<User> all = userService.getAll();
        System.out.println(all);

        List<User> users = userService.filterByAge();
        System.out.println(users);

        List<User> users1 = userService.filterByLastNamePostfix();
        System.out.println(users1);

        userService.update(UpdateUserDto.builder()
                .username("oddishwolf")
                .newUsername("username1")
                .birthday("24-11-1998")
                .email("wanttoworkindg@gmail.com")
                .build());

        Optional<User> oddishwolf = userService.get("oddishwolf");
        System.out.println(oddishwolf);
    }
}
