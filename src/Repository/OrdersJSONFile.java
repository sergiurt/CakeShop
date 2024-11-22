package Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import Domain.Order;
import Domain.Cake;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class OrdersJSONFile implements IRepository<Integer, Order> {
    private final String filePath;
    private final Map<Integer, Order> orders = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IRepository<Integer, Cake> cakeRepo;

    public OrdersJSONFile(String filePath, IRepository<Integer, Cake> cakeRepo) {
        this.filePath = filePath;
        this.cakeRepo = cakeRepo;
        loadOrders();
    }

    private void loadOrders() {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Order[] ordersArray = objectMapper.readValue(file, Order[].class);
                for (Order order : ordersArray) {
                    orders.put(order.getId(), order);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load orders from JSON file: " + e.getMessage());
        }
    }

    private void saveOrders() {
        try {
            objectMapper.writeValue(new File(filePath), orders.values());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save orders to JSON file: " + e.getMessage());
        }
    }

    @Override
    public void addEntity(Integer id, Order order) {
        orders.put(id, order);
        saveOrders();
    }

    @Override
    public void updateEntity(Integer id, Order order) {
        orders.put(id, order);
        saveOrders();
    }

    @Override
    public Optional<Order> deleteEntity(Integer id) {
        if (orders.containsKey(id)) {
            Order order = orders.get(id);
            orders.remove(id);
            saveOrders();
            return Optional.of(order);
        }
        orders.remove(id);
        saveOrders();
        return null;
    }

    @Override
    public Optional<Order> findEntity(Integer id) {

        Order orderToFind = orders.get(id);
        return Optional.ofNullable(orderToFind);
    }

    @Override
    public Iterator<Order> findAllEntities() {
        return orders.values().iterator();
    }
}
