package Repository;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Optional;

public class Repository<ID,T> implements IRepository<ID,T> {
    protected HashMap<ID,T> map = new HashMap<>();

    @Override
    public void addEntity(ID id, T t) {
        map.put(id, t);
    }
    @Override
    public Optional<T> deleteEntity(ID id) {
        T entityToDelete = map.get(id);
        if(entityToDelete != null) {
            map.remove(id);
        }
        return Optional.ofNullable(entityToDelete);
    }
    @Override
    public void updateEntity(ID id, T t) {
        map.put(id, t);
    }
    @Override
    public Optional<T> findEntity(ID id) {
        T entityToFind = map.get(id);
        return Optional.ofNullable(entityToFind);
    }
    @Override
    public Iterator<T> findAllEntities() {
        return map.values().iterator();
    }


}
