package Repository;

import Filter.AbstractFilter;
import java.util.ArrayList;
import java.util.Iterator;

public class FilteredRepository<ID, T> extends Repository<ID, T> {
    private AbstractFilter<T> filter;

    // Constructor accepts a filter
    public FilteredRepository(AbstractFilter<T> filter) {
        this.filter = filter;
    }

    // Overrides method to return filtered entities
    @Override
    public Iterator<T> findAllEntities() {
        ArrayList<T> result = new ArrayList<>();
        for(T entity: this.map.values()){
            if(filter.accept(entity)){
                result.add(entity);
            }
        }
        return result.iterator();
    }
}
