package Repository;

import java.util.Iterator;
import java.util.Optional;

public interface IRepository<ID, T> {

    void addEntity(ID id, T t);
    void updateEntity(ID id, T t);
    Optional<T> deleteEntity(ID id);
    Optional<T> findEntity(ID id);
    Iterator<T> findAllEntities();

}
