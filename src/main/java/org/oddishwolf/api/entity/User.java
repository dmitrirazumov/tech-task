package org.oddishwolf.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    String username;
    String firstName;
    String lastName;
    LocalDate birthday;
    String email;
    Gender gender;

    //contains additional options. for example: new username
    String additionalParameter;
}
