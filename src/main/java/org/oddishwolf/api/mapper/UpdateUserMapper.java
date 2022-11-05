package org.oddishwolf.api.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.util.LocalDateFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserMapper implements Mapper<UpdateUserDto, User> {

    private static final UpdateUserMapper INSTANCE = new UpdateUserMapper();

    @Override
    public User mapFrom(UpdateUserDto object) {
        return User.builder()
                .username(object.getUsername())
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .birthday(LocalDateFormatter.format(object.getBirthday()))
                .email(object.getEmail())
                .gender(findGenderByStringId(object))
                .build();
    }

    private Gender findGenderByStringId(UpdateUserDto object) {
        Gender gender = null;
        if (object.getGender().equals("1"))
            gender = Gender.MALE;
        if (object.getGender().equals("2"))
            gender = Gender.FEMALE;
        return gender;
    }

    public static UpdateUserMapper getInstance() {
        return INSTANCE;
    }
}