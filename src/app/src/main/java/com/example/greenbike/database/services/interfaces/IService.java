package com.example.greenbike.database.services.interfaces;

import java.util.List;

public interface IService<T> {
    T create(T entity);

    T getById(String id);

    List<T> getAll();

    T update(T entity);
}
