package org.oddishwolf.api.mapper;

public interface Mapper<F, T> {
    T mapFrom(F object);
}
