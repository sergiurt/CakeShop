package Repository;

import Domain.Order;
import Domain.Cake;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@XmlRootElement(name = "orders")
public class OrdersXMLFile implements IRepository<Integer, Order> {
    private final String filePath;
    private final Map<Integer, Order> orders = new HashMap<>();
    private final IRepository<Integer, Cake> cakeRepo;

    // Default constructor required for JAXB
    public OrdersXMLFile() {
        this.filePath = null; // This will be set in the parameterized constructor
        this.cakeRepo = null; // This will be set in the parameterized constructor
    }

    // Constructor accepting file path and cake repository
    public OrdersXMLFile(String filePath, IRepository<Integer, Cake> cakeRepo) {
        this.filePath = filePath;
        this.cakeRepo = cakeRepo;
        loadOrders();
    }

    private void loadOrders() {
        try {
            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {  // Check if file is not empty
                JAXBContext context = JAXBContext.newInstance(OrdersXMLFile.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                OrdersXMLFile ordersXMLFile = (OrdersXMLFile) unmarshaller.unmarshal(file);
                for (Order order : ordersXMLFile.getOrders()) {
                    orders.put(order.getId(), order);
                }
            }
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to load orders from XML file: " + e.getMessage());
        }
    }


    // Save orders to the XML file
    private void saveOrders() {
        try {
            JAXBContext context = JAXBContext.newInstance(OrdersXMLFile.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(this, new File(filePath));
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to save orders to XML file: " + e.getMessage());
        }
    }

    @XmlElement(name = "order")
    public Order[] getOrders() {
        return orders.values().toArray(new Order[0]); // Convert the map values to an array
    }

    public void setOrders(Map<Integer, Order> orders) {
        this.orders.clear();
        this.orders.putAll(orders);
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
        if(orders.containsKey(id)) {
            orders.remove(id);
            saveOrders();
            return Optional.of(orders.get(id));
        }
        return null;
    }

    @Override
    public Optional<Order> findEntity(Integer id) {
        return Optional.of(orders.get(id));
    }

    @Override
    public Iterator<Order> findAllEntities() {
        return orders.values().iterator();
    }
}
