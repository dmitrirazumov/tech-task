package org.oddishwolf.api.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oddishwolf.api.dao.UserDao;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.exception.ValidationException;
import org.oddishwolf.api.mapper.UpdateUserMapper;
import org.oddishwolf.api.validator.Error;
import org.oddishwolf.api.validator.UpdateUserValidator;
import org.oddishwolf.api.validator.ValidationResult;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("fast")
@Tag("service_user")
@ExtendWith({MockitoExtension.class})
public class UserServiceTest {

    private static final User FAKE_USER1 =
            User.builder().username("un1").firstName("fn1").lastName("Датаджайлов").birthday(LocalDate.of(Year.now().getValue(), 6, 1)).email("e1@g.com").gender(Gender.MALE).build();
    private static final User FAKE_USER2 =
            User.builder().username("un2").firstName("fn2").lastName("Датаджайлев").birthday(LocalDate.of(Year.now().getValue() - 25, 6, 2)).email("e2@g.com").gender(Gender.FEMALE).build();
    private static final UpdateUserDto FAKE_UPDATE_USER_DTO1 =
            UpdateUserDto.builder().username("un1").email("e3@g.com").build();

    @Mock
    private UserDao userDao;
    @Mock
    private UpdateUserValidator updateUserValidator;
    @Mock
    private UpdateUserMapper updateUserMapper;
    @InjectMocks
    private UserService userService;

    @Nested
    @Tag("service_user_get")
    class GetMethodsTest {
        @Test
        void getReturnTrue() {
            doReturn(Optional.of(FAKE_USER1)).when(userDao).findById(FAKE_USER1.getUsername());
            Optional<User> maybeUser = userService.get(FAKE_USER1.getUsername());
            assertThat(maybeUser).isPresent();
            maybeUser.ifPresent(user -> assertThat(user).isEqualTo(FAKE_USER1));
        }
        @Test
        void getReturnFalse() {
            doReturn(Optional.empty()).when(userDao).findById(FAKE_USER2.getUsername());
            Optional<User> maybeUser = userService.get(FAKE_USER2.getUsername());
            assertThat(maybeUser).isEmpty();
        }
        @Test
        void getAllReturnTrue() {
            doReturn(List.of(FAKE_USER1, FAKE_USER2)).when(userDao).findAll();
            List<User> maybeUsers = userService.getAll();
            assertThat(maybeUsers).isNotEmpty();
            assertThat(maybeUsers).contains(FAKE_USER1, FAKE_USER2);
        }
        @Test
        void getAllReturnFalse() {
            doReturn(new ArrayList<User>()).when(userDao).findAll();
            List<User> maybeUsers = userService.getAll();
            assertThat(maybeUsers).isEmpty();
        }
    }

    @Nested
    @Tag("service_user_update")
    class UpdateTest {
        @Test
        void updateReturnTrue() {
            User updateUser = User.builder().username("un1").email("e3@g.com").build();
            doReturn(new ValidationResult()).when(updateUserValidator).isValid(FAKE_UPDATE_USER_DTO1);
            doReturn(updateUser).when(updateUserMapper).mapFrom(FAKE_UPDATE_USER_DTO1);
            doReturn(true).when(userDao).update(updateUser);

            boolean update = userService.update(FAKE_UPDATE_USER_DTO1);
            assertThat(update).isTrue();
        }
        @Test
        void updateReturnFalse() {
            ValidationResult result = new ValidationResult();
            result.add(Error.of("dummy", "dummy"));
            doReturn(result).when(updateUserValidator).isValid(FAKE_UPDATE_USER_DTO1);

            assertThrows(ValidationException.class,
                    () -> userService.update(FAKE_UPDATE_USER_DTO1));
        }
    }

    @Nested
    @Tag("service_user_filters")
    class FilterTest {
        @Test
        void filterByAge() {
            doReturn(List.of(FAKE_USER1, FAKE_USER2)).when(userDao).findAll();
            List<User> maybeUsers = userService.filterUsersByAge();
            assertThat(maybeUsers).hasSize(1);
            assertThat(maybeUsers).contains(FAKE_USER1);
            assertThat(maybeUsers).doesNotContain(FAKE_USER2);
        }
        @Test
        void filterByLastNamePostfix() {
            doReturn(List.of(FAKE_USER1)).when(userDao).findAll();
            long countOfUsers = userService.countUsersByLastNamePostfix();
            assertThat(countOfUsers).isEqualTo(1);
        }
    }

    @Nested
    @Tag("service_user_init_db")
    class InitData {
        @Test
        void initReturnTrue() {
            doReturn(2).when(userDao).createTablesAndInsertData();
            int init = userService.init();
            assertThat(init).isEqualTo(2);
        }
        @Test
        void initReturnFalse() {
            doReturn(0).when(userDao).createTablesAndInsertData();
            int init = userService.init();
            assertThat(init).isEqualTo(0);
        }
        @Test
        void initTablesReturnTrue() {
            doReturn(true).when(userDao).createTables();
            boolean init = userService.initTables();
            assertThat(init).isTrue();
        }
        @Test
        void initTablesReturnFalse() {
            doReturn(false).when(userDao).createTables();
            boolean init = userService.initTables();
            assertThat(init).isFalse();
        }
        @Test
        void initDataTrue() {
            doReturn(1).when(userDao).insertInitData();
            int init = userService.initData();
            assertThat(init).isEqualTo(1);
        }
        @Test
        void initDataFalse() {
            doReturn(0).when(userDao).insertInitData();
            int init = userService.initData();
            assertThat(init).isEqualTo(0);
        }
    }

    @Test
    void getAge() {
        long age = userService.getAge(FAKE_USER1);
        assertThat(Year.now().getValue() - FAKE_USER1.getBirthday().getYear()).isEqualTo(age);
    }
}
