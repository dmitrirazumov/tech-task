package org.oddishwolf.api.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class UserServiceTest {

    private static final User IVAN =
            User.builder().username("un1").firstName("fn1").lastName("ln1").birthday(LocalDate.of(2000, 6, 1)).email("e1@g.com").gender(Gender.MALE).build();

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    @Nested
    @Tag("get")
    class GetTest {

        @Test
        void getReturnTrue() {
            doReturn(Optional.of(IVAN)).when(userDao).findById(IVAN.getUsername());
            Optional<User> user = userService.get(IVAN.getUsername());
            assertThat(user).isPresent();
        }
    }
}
