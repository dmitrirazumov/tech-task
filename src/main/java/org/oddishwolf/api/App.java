package org.oddishwolf.api;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.service.UserService;

import java.util.Optional;

public class App {
    public static void main(String[] args) {

        Optional<User> username1 = UserDao.getInstance().findById("username1");
        if (username1.isPresent())
            System.out.println(username1);

        boolean update = UserService.getInstance().update(UpdateUserDto.builder()
                .username("username3")
                .newUsername("oddishwolf2")
                .firstName("Gile")
//                .lastName("Data")
                .birthday("04-12-2019")
//                .email("test@gmail.com")
                .gender("1")
                .build());

        Optional<User> username2 = UserDao.getInstance().findById("oddishwolf2");
        if (username2.isPresent())
            System.out.println(username2);

        System.out.println(update);
    }
}
