package org.oddishwolf.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateUserDto {
    String username;
    String firstName;
    String lastName;
    String birthday;
    String email;
    String gender;

    public boolean isEmpty() {
        return firstName == null
                && lastName == null
                && birthday == null
                && email == null
                && gender == null;
    }
}
