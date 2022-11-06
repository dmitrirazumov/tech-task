package org.oddishwolf.api.mapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.Gender;
import org.oddishwolf.api.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
@Tag("mapper_update_user_dto")
@ExtendWith({MockitoExtension.class})
public class UpdateUserMapperTest {

    private static final List<UpdateUserDto> FAKE_USERS_DTO = List.of(
            //for correct work
            UpdateUserDto.builder().username("un1").build(),
            UpdateUserDto.builder().username("un2").firstName("fn2").build(),
            UpdateUserDto.builder().username("un3").firstName("fn3").lastName("ln1").build(),
            UpdateUserDto.builder().username("un4").firstName("fn4").birthday("01-01-2000").build(),
            UpdateUserDto.builder().username("un5").firstName("fn5").birthday("01-01-2000").email("em1").build(),
            UpdateUserDto.builder().username("un6").firstName("fn6").birthday("01-01-2000").email("em1").gender("1").build(),

            //for incorrect work
            UpdateUserDto.builder().username("un7").birthday("1-13-nope").build(),
            UpdateUserDto.builder().username("un8").birthday("80-1-23").build(),
            UpdateUserDto.builder().username("un9").birthday("1-15-1997").gender("200").build(),
            UpdateUserDto.builder().username("un10").birthday("1-1-1998").build(),
            UpdateUserDto.builder().username("un11").birthday("").build()
    );
    private static final List<User> FAKE_USERS = List.of(
            //for correct work
            User.builder().username("un1").build(),
            User.builder().username("un2").firstName("fn2").build(),
            User.builder().username("un3").firstName("fn3").lastName("ln1").build(),
            User.builder().username("un4").firstName("fn4").birthday(LocalDate.of(2000, 1, 1)).build(),
            User.builder().username("un5").firstName("fn5").birthday(LocalDate.of(2000, 1, 1)).email("em1").build(),
            User.builder().username("un6").firstName("fn6").birthday(LocalDate.of(2000, 1, 1)).email("em1").gender(Gender.MALE).build()
    );

    @InjectMocks
    UpdateUserMapper updateUserMapper;

    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.mapper.UpdateUserMapperTest#getArgumentsForMapFromCorrect")
    void mapFromCorrect(UpdateUserDto fakeUserDto, User fakeUser) {
        User user = updateUserMapper.mapFrom(fakeUserDto);
        assertThat(user).isEqualTo(fakeUser);
    }

    //found only one option for incorrect work -> DateTimeParseException
    //validation step will check most situations
    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.mapper.UpdateUserMapperTest#getArgumentsForMapFromIncorrect")
    void mapFromIncorrect(UpdateUserDto fakeUserDto) {
        assertThrows(DateTimeParseException.class,
                () -> updateUserMapper.mapFrom(fakeUserDto));
    }

    static Stream<Arguments> getArgumentsForMapFromCorrect() {
        return Stream.of(
                Arguments.of(FAKE_USERS_DTO.get(0), FAKE_USERS.get(0)),
                Arguments.of(FAKE_USERS_DTO.get(1), FAKE_USERS.get(1)),
                Arguments.of(FAKE_USERS_DTO.get(2), FAKE_USERS.get(2)),
                Arguments.of(FAKE_USERS_DTO.get(3), FAKE_USERS.get(3)),
                Arguments.of(FAKE_USERS_DTO.get(4), FAKE_USERS.get(4)),
                Arguments.of(FAKE_USERS_DTO.get(5), FAKE_USERS.get(5))
        );
    }

    static Stream<Arguments> getArgumentsForMapFromIncorrect() {
        return Stream.of(
                Arguments.of(FAKE_USERS_DTO.get(6)),
                Arguments.of(FAKE_USERS_DTO.get(7)),
                Arguments.of(FAKE_USERS_DTO.get(8)),
                Arguments.of(FAKE_USERS_DTO.get(9)),
                Arguments.of(FAKE_USERS_DTO.get(10))
        );
    }
}
