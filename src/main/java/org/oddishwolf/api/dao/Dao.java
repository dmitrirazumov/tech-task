package org.oddishwolf.api.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, T> {

    boolean save(T entity);

    List<T> findAll();

    Optional<T> findById(K key);

    boolean update(T entity);

    boolean delete(K id);
}
