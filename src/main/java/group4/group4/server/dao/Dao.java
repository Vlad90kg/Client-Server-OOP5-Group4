package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;

import java.util.Comparator;
import java.util.List;

public interface Dao <T> {

    List<T> getAll() throws DaoException;
    T getById(int id)throws DaoException;
    T insert(T t) throws DaoException;
    int update(int id, T t) throws DaoException;
    int delete(int id)throws DaoException;
    List<T> findByFilter(Comparator<T> comparator, double treshold) throws DaoException;


}
