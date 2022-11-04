package org.oddishwolf.api;

import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.service.UserService;

import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {

        Optional<User> username1 = UserService.getInstance().get("username1");
        List<User> users = UserService.getInstance().getAll();
        List<User> filteredUsersByAge = UserService.getInstance().filterByAge();
        List<User> filteredUsersByLastName = UserService.getInstance().filterByLastNamePostfix();

        System.out.println(username1);
        System.out.println(users);
        System.out.println(filteredUsersByAge);
        System.out.println(filteredUsersByLastName);
    }
}
