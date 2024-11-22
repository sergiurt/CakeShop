package Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import Domain.Cake;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class CakesJSONFile implements IRepository<Integer, Cake> {
    private final String filePath;
    private final Map<Integer, Cake> cakes = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CakesJSONFile(String filePath) {
        this.filePath = filePath;
        loadCakes();
    }

    private void loadCakes() {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Cake[] cakesArray = objectMapper.readValue(file, Cake[].class);
                for (Cake cake : cakesArray) {
                    cakes.put(cake.getId(), cake);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load cakes from JSON file: " + e.getMessage());
        }
    }

    private void saveCakes() {
        try {
            objectMapper.writeValue(new File(filePath), cakes.values());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save cakes to JSON file: " + e.getMessage());
        }
    }

    @Override
    public void addEntity(Integer id, Cake cake) {
        cakes.put(id, cake);
        saveCakes();
    }

    @Override
    public void updateEntity(Integer id, Cake cake) {
        cakes.put(id, cake);
        saveCakes();
    }

    @Override
    public Optional<Cake> deleteEntity(Integer id) {
        if(cakes.containsKey(id)) {
            Cake cake = cakes.get(id);
            cakes.remove(id);
            saveCakes();
            return Optional.of(cake);
        }
        return null;
    }

    @Override
    public Optional<Cake> findEntity(Integer id) {
        Cake CaketoFind = cakes.get(id);
        return Optional.ofNullable(CaketoFind);
    }

    @Override
    public Iterator<Cake> findAllEntities() {
        return cakes.values().iterator();
    }
}
