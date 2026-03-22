package ru.msu.cmc.webprac.dao;

import java.util.List;

public interface CommonDAO<T, ID> {
    List<T> getAll();
    T getById(ID id);
    T save(T entity);
    T update(T entity);
    void delete(ID id);
}