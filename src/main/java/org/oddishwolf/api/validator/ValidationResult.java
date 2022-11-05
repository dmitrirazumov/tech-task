package org.oddishwolf.api.validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    @Getter
    private final List<Error> exceptions = new ArrayList<>();

    public void add(Error exc) {
        this.exceptions.add(exc);
    }

    public boolean isValid() {
        return exceptions.isEmpty();
    }
}
