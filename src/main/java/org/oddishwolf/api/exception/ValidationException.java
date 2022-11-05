package org.oddishwolf.api.exception;

import lombok.Getter;
import org.oddishwolf.api.validator.Error;

import java.util.List;

public class ValidationException extends RuntimeException {

    @Getter
    private final List<Error> errors;

    public ValidationException(List<Error> errors) {
        this.errors = errors;
    }
}
