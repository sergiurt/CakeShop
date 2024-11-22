package Repository;

import Domain.Order;
import Domain.Cake;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


public class OrdersDB implements IRepository<Integer, Order> {
    private final String URL;
    private final Connection connection;
    private final IRepository<Integer, Cake> cakeShop;

    public OrdersDB(String URL, IRepository<Integer, Cake> cakeShop) {
        this.URL = URL;
        this.cakeShop = cakeShop;
        try {
            this.connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage());
        }
    }

    @Override
    public void addEntity(Integer id, Order order) {
        String sql = "INSERT INTO Orders (id, cake_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, order.getCake().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add order: " + e.getMessage());
        }
    }

    @Override
    public Optional<Order> findEntity(Integer id) {
        String sql = "SELECT * FROM Orders WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int cakeId = resultSet.getInt("cake_id");
                Cake cake = cakeShop.findEntity(cakeId).orElseThrow(() -> new IllegalArgumentException("Cake not found"));
                return Optional.ofNullable(new Order(id, cake));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(Integer id, Order order) {
        String sql = "UPDATE Orders SET cake_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, order.getCake().getId());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order: " + e.getMessage());
        }
    }

    @Override
    public Optional<Order> deleteEntity(Integer id) {
        String sql = "DELETE FROM Orders WHERE id = ?";
        if(findEntity(id).isPresent()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete order: " + e.getMessage());
            }
            return findEntity(id);
        }
        return null;
    }

    @Override
    public Iterator<Order> findAllEntities() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int cakeId = resultSet.getInt("cake_id");
                Optional<Cake> cake = cakeShop.findEntity(cakeId);
                Cake cake1 = cake.orElseThrow(() -> new IllegalArgumentException("Cake not found"));
                orders.add(new Order(id, cake1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all orders: " + e.getMessage());
        }
        return orders.iterator();
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection: " + e.getMessage());
        }
    }


}
