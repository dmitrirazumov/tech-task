package org.oddishwolf.api.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
@Tag("util_localDateFormatter")
public class LocalDateFormatterTest {

    private static final List<String> FAKE_DATES = List.of(
            "01-01-1998", "02-02-2004", "05-11-2011", "15-07-1998", "30-03-2000",
            "1-13-nope", "80-1-23", "1-15-1997", "1-1-1998", "0-0-0"
    );
    private static final List<LocalDate> FAKE_LOCAL_DATES = List.of(
            LocalDate.of(1998, 1, 1),
            LocalDate.of(2004, 2, 2),
            LocalDate.of(2011, 11, 5),
            LocalDate.of(1998, 7, 15),
            LocalDate.of(2000, 3, 30)
    );

    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.util.LocalDateFormatterTest#getArgumentsForFormatAndIsValidTrue")
    void formatReturnNotNull(String fakeDate, LocalDate fakeLocalDate) {
        LocalDate format = LocalDateFormatter.format(fakeDate);
        assertThat(format).isNotNull();
        assertThat(format).isEqualTo(fakeLocalDate);
    }

    @ParameterizedTest
    @NullSource
    void formatReturnNull(String nulls) {
        LocalDate format = LocalDateFormatter.format(nulls);
        assertThat(format).isNull();
    }

    @ParameterizedTest
    @EmptySource
    void formatReturnExceptionIfStringIsEmpty(String empty) {
        assertThrows(DateTimeParseException.class,
                () -> LocalDateFormatter.format(empty));
    }

    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.util.LocalDateFormatterTest#getArgumentsForFormatAndIsValidFalse")
    void formatReturnExceptionIfStringHasWrongFormat(String fakeDates) {
        assertThrows(DateTimeParseException.class,
                () -> LocalDateFormatter.format(fakeDates));
    }

    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.util.LocalDateFormatterTest#getArgumentsForFormatAndIsValidTrue")
    void isValidReturnTrue(String fakeDates) {
        boolean isValid = LocalDateFormatter.isValid(fakeDates);
        assertThat(isValid).isTrue();
    }

    @ParameterizedTest
    @MethodSource("org.oddishwolf.api.util.LocalDateFormatterTest#getArgumentsForFormatAndIsValidFalse")
    void isValidReturnFalse(String fakeDates) {
        boolean isValid = LocalDateFormatter.isValid(fakeDates);
        assertThat(isValid).isFalse();
    }

    static Stream<Arguments> getArgumentsForFormatAndIsValidTrue() {
        return Stream.of(
                Arguments.of(FAKE_DATES.get(0), FAKE_LOCAL_DATES.get(0)),
                Arguments.of(FAKE_DATES.get(1), FAKE_LOCAL_DATES.get(1)),
                Arguments.of(FAKE_DATES.get(2), FAKE_LOCAL_DATES.get(2)),
                Arguments.of(FAKE_DATES.get(3), FAKE_LOCAL_DATES.get(3)),
                Arguments.of(FAKE_DATES.get(4), FAKE_LOCAL_DATES.get(4))
        );
    }

    static Stream<Arguments> getArgumentsForFormatAndIsValidFalse() {
        return Stream.of(
                Arguments.of(FAKE_DATES.get(5)),
                Arguments.of(FAKE_DATES.get(6)),
                Arguments.of(FAKE_DATES.get(7)),
                Arguments.of(FAKE_DATES.get(8)),
                Arguments.of(FAKE_DATES.get(9))
        );
    }
}
