package org.oddishwolf.api.validator;

import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.service.UserService;
import org.oddishwolf.api.util.LocalDateFormatter;

import java.util.Set;

public class UpdateUserValidator implements Validator<UpdateUserDto> {

    private final UserService userService = new UserService(new UserDao());

    private static final Set<String> GENDERS_CODE = Set.of("1", "2");

    @Override
    public ValidationResult isValid(UpdateUserDto object) {
        ValidationResult validationResult = new ValidationResult();

        if (object.getUsername() == null || object.getUsername().isBlank()) {
            validationResult.add(Error.of("blank.username", "You should type username"));
            return validationResult;
        }

        if (userService.get(object.getUsername()).isEmpty()) {
            validationResult.add(Error.of("incorrect.username", "User doesn't exist"));
            return validationResult;
        }

        if (object.isEmpty()) {
            validationResult.add(Error.of("blank.fields", "You must fill in at least one field"));
        }

        if (object.getNewUsername() != null && userService.get(object.getNewUsername()).isPresent()) {
            validationResult.add(Error.of("already.exist", "This username already exist. Try to type another"));
        }

        if (object.getBirthday() != null && !LocalDateFormatter.isValid(object.getBirthday())) {
            validationResult.add(Error.of("incorrect.date", "Type date in format dd-MM-yyyy"));
        }

        if (object.getGender() != null && GENDERS_CODE.stream().noneMatch(code -> object.getGender().equals(code))) {
            validationResult.add(Error.of("incorrect.gender.code", "You should type number: MALE - 1, FEMALE - 2"));
        }

        return validationResult;
    }
}
