package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public interface Dao <T> {

    List<T> getAll() throws DaoException;
    T getById(int id)throws DaoException;
    T insert(T t) throws SQLException;
    int update(int id, T t) throws DaoException;
    int delete(int id)throws DaoException;
    List<T> findByFilter(Comparator<T> comparator, double treshold) throws DaoException;
}
