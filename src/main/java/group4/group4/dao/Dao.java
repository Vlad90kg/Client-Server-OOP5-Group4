package group4.group4.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.dto.MobilePhone;

import java.util.Comparator;
import java.util.List;

public interface Dao <T> {

    List<T> getAll() throws DaoException;
    T getById(int id);
    T insert(T t);
    void update(int id, T t);
    void delete(int id);
    List<T> findByFilter(Comparator<T> comparator) throws DaoException;


}
