package org.oddishwolf.api.mapper;

import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.util.LocalDateFormatter;

public class UpdateUserMapper implements Mapper<UpdateUserDto, User> {

    @Override
    public User mapFrom(UpdateUserDto object) {
        return User.builder()
                .username(object.getUsername())
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .birthday(LocalDateFormatter.format(object.getBirthday()))
                .email(object.getEmail())
                .gender(findGenderByStringId(object))
                .additionalParameter(object.getNewUsername())
                .build();
    }

    private Gender findGenderByStringId(UpdateUserDto object) {
        Gender gender = null;
        if (object.getGender() != null && object.getGender().equals("1"))
            gender = Gender.MALE;
        if (object.getGender() != null && object.getGender().equals("2"))
            gender = Gender.FEMALE;
        return gender;
    }
}