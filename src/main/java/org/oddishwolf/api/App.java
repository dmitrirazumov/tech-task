package org.oddishwolf.api;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.entity.User;

import java.util.List;

public class App {
    public static void main(String[] args) {

        List<User> users = UserDao.getInstance().findAll();
        System.out.println(users);

    }
}
