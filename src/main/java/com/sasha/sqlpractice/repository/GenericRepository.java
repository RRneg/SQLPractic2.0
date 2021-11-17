package com.sasha.sqlpractice.repository;

import java.util.List;


public interface GenericRepository<T, ID> {



    T getById(ID id);

    T update(T t);

    void deleteById(ID id);

    List<T> getAll();



}
