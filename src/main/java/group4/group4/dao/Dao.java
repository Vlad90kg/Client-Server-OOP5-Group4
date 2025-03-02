package group4.group4.dao;

import java.util.List;

public interface Dao <T> {

    List<T> getAll();
    T getById(int id);
    T insert(T t);
    void update(int id, T t);

}
