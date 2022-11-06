package org.oddishwolf.api;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.mapper.UpdateUserMapper;
import org.oddishwolf.api.service.UserService;
import org.oddishwolf.api.validator.UpdateUserValidator;
import org.oddishwolf.api.validator.ValidationResult;

public class App {
    public static void main(String[] args) {

        new UserService(new UserDao(), new UpdateUserValidator(), new ValidationResult(), new UpdateUserMapper()).init();
    }
}
