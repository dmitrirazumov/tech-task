package org.oddishwolf.api;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.service.UserService;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        boolean init = UserService.getInstance().init();
        System.out.println(init);
    }
}
