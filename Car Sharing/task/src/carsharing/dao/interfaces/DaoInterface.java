package carsharing.dao.interfaces;

import java.util.List;

public interface DaoInterface<T> {
    List<T> findAll();
    T findById(int id);
    void add(T developer);
    void update(T developer);
    void deleteById(int id);
}
