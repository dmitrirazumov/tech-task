package org.oddishwolf.api.validator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("fast")
@Tag("validator_update_user_dto")
@ExtendWith({MockitoExtension.class})
public class UpdateUserValidatorTest {

    @Mock
    UserService userService;
    @InjectMocks
    UpdateUserValidator updateUserValidator;

    private static final List<UpdateUserDto> FAKE_USERS_DTO = List.of(
            //for incorrect result
            UpdateUserDto.builder().build(),
            UpdateUserDto.builder().username("      ").build(),
            UpdateUserDto.builder().username("userNotExist").build(),
            UpdateUserDto.builder().username("username1").build(),
            UpdateUserDto.builder().username("username1").newUsername("up_username1").build(),
            UpdateUserDto.builder().username("username1").birthday("1998-1-13").build(),

            //for correct result
            UpdateUserDto.builder().username("username1").birthday("24-11-1998").build(),
            UpdateUserDto.builder().username("username2").firstName("giledata").email("dummy").build(),
            UpdateUserDto.builder().username("username1").birthday("24-11-1998").build(),
            UpdateUserDto.builder().username("username1").newUsername("username2").build(),
            UpdateUserDto.builder().username("username1").firstName("first_name1").lastName("Васильков").birthday("24-11-1998").email("email1@java.com").gender("1").build()
    );

    @Test
    void isValidReturnExceptionsIfUsernameIsEmpty() {
        ValidationResult valid = updateUserValidator.isValid(FAKE_USERS_DTO.get(0));
        assertThat(valid.isValid()).isFalse();
    }

    @Test
    void isValidReturnExceptionsIfUsernameIsBlank() {
        ValidationResult valid = updateUserValidator.isValid(FAKE_USERS_DTO.get(1));
        assertThat(valid.isValid()).isFalse();
    }

    @Test
    void isValidReturnExceptionsIfUserDoesnExistInDb() {
        doReturn(Optional.empty()).when(userService).get(FAKE_USERS_DTO.get(2).getUsername());
        ValidationResult valid = updateUserValidator.isValid(FAKE_USERS_DTO.get(2));
        assertThat(valid.isValid()).isFalse();
    }

    @Test
    void isValidReturnExceptionsIfFieldsAreEmpty() {
        doReturn(Optional.of(FAKE_USERS_DTO.get(3))).when(userService).get(FAKE_USERS_DTO.get(3).getUsername());
        ValidationResult valid = updateUserValidator.isValid(FAKE_USERS_DTO.get(3));
        assertThat(valid.isValid()).isFalse();
    }

    @Test
    void isValidReturnExceptionsIfNewUsernameAlreadyExist() {
        doReturn(Optional.of(FAKE_USERS_DTO.get(4))).when(userService).get(FAKE_USERS_DTO.get(4).getUsername());
        doReturn(Optional.of(FAKE_USERS_DTO.get(4))).when(userService).get(FAKE_USERS_DTO.get(4).getNewUsername());
        ValidationResult valid = updateUserValidator.isValid(FAKE_USERS_DTO.get(4));
        assertThat(valid.isValid()).isFalse();
    }

    @Test
    void isValidReturnExceptionsIfDateHasWrongFormat() {
        doReturn(Optional.of(FAKE_USERS_DTO.get(5))).when(userService).get(FAKE_USERS_DTO.get(5).getUsername());
        ValidationResult valid = updateUserValidator.isValid(FAKE_USERS_DTO.get(5));
        assertThat(valid.isValid()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "3", "4", "24", "1000", "20"})
    void isValidReturnExceptionsIfGenderHasWrongCode(String fakeGender) {
        UpdateUserDto fakeUserDto = UpdateUserDto.builder().username("dummy").gender(fakeGender).build();
        doReturn(Optional.of(fakeUserDto)).when(userService).get(fakeUserDto.getUsername());
        ValidationResult valid = updateUserValidator.isValid(fakeUserDto);
        assertThat(valid.isValid()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.validator.UpdateUserValidatorTest#getArgumentsForIsValidDidCorrect")
    void isValidDidCorrect(UpdateUserDto fakeUpdateUserDto) {
        doReturn(Optional.of(fakeUpdateUserDto)).when(userService).get(fakeUpdateUserDto.getUsername());
//        doReturn(Optional.empty()).when(userService).get(fakeUpdateUserDto.getNewUsername());
        ValidationResult valid = updateUserValidator.isValid(fakeUpdateUserDto);
        assertThat(valid.isValid()).isTrue();
    }

    static Stream<Arguments> getArgumentsForIsValidDidCorrect() {
        return Stream.of(
                Arguments.of(FAKE_USERS_DTO.get(6)),
                Arguments.of(FAKE_USERS_DTO.get(7)),
                Arguments.of(FAKE_USERS_DTO.get(8)),
                Arguments.of(FAKE_USERS_DTO.get(9)),
                Arguments.of(FAKE_USERS_DTO.get(10))
        );
    }
}
