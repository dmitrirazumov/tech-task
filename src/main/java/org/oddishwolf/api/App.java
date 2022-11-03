package org.oddishwolf.api;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.entity.User;

import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {

        List<User> users = UserDao.getInstance().findAll();
        Optional<User> user = UserDao.getInstance().findById("username");

        System.out.println(users);
        System.out.println(user);

    }
}
