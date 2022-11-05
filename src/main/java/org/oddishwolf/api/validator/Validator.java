package org.oddishwolf.api.validator;

public interface Validator<T> {
    ValidationResult isValid(T object);
}
