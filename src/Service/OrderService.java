package Service;

import Domain.Order;
import Exceptions.OrderExceptions;
import Repository.FilteredRepository;
import Repository.IRepository;
import Filter.FilterOrderByName;
import Filter.FilterOrderByQuantity;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderService {
    private IRepository<Integer, Order> orders;
    private CakeService cakeService;

    public OrderService(IRepository<Integer, Order> orders, CakeService cakeService) {
        this.orders = orders;
        this.cakeService = cakeService;
    }

    public void add(Integer id, Order order) {
        if (id == null) {
            throw new OrderExceptions("id cannot be null or empty");
        }
        if (orders.findEntity(id).isPresent()) {
            throw new OrderExceptions("An order with id " + id + " already exists");
        }
        if (cakeService.showCake(order.getCake().getId()).isEmpty()) {
            throw new OrderExceptions("Cake with id " + order.getCake().getId() + " does not exist");
        }
        orders.addEntity(id, order);
    }

    public Optional<Order> remove(Integer id) {
        if (id == null) {
            throw new OrderExceptions("id cannot be null or empty");
        }
        if (orders.findEntity(id).isEmpty()) {
            throw new OrderExceptions("Order with id " + id + " does not exist");
        }
        return orders.deleteEntity(id);
    }

    public void update(Integer id, Order order) {
        if (id == null) {
            throw new OrderExceptions("id cannot be null or empty");
        }
        if (orders.findEntity(id).isEmpty()) {
            throw new OrderExceptions("An order with id " + id + " does not exist");
        }
        if (cakeService.showCake(order.getCake().getId()).isEmpty()) {
            throw new OrderExceptions("Cake with id " + order.getCake().getId() + " does not exist");
        }
        orders.updateEntity(id, order);
    }

    public Optional<Order> showOrder(Integer id) {
        if (id == null) {
            throw new OrderExceptions("id cannot be null or empty");
        }
        return Optional.of(orders.findEntity(id).orElseThrow(() -> new OrderExceptions("Order with id " + id + " does not exist")));
    }

    public ArrayList<Order> getOrders() {
        ArrayList<Order> orderList = new ArrayList<>();
        orders.findAllEntities().forEachRemaining(orderList::add);
        if (orderList.isEmpty()) {
            throw new OrderExceptions("No orders found");
        }
        return orderList;
    }


    // Using Streams for cleaner code and filtering directly
    public ArrayList<Order> getOrdersByQuantity(int quantity) {
        return getOrders().stream()
                .filter(order -> order.getQuantity() >= quantity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Order> getOrdersByName(String name) {
        return getOrders().stream()
                .filter(order -> order.getCake().getName().equalsIgnoreCase(name))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
