package Repository;

import Domain.Cake;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CakesDB implements IRepository<Integer, Cake> {
    private final String URL;
    private final Connection connection;

    public CakesDB(String URL) {
        this.URL = URL;
        try {
            this.connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage());
        }

    }

    @Override
    public void addEntity(Integer id, Cake cake) {
        String sql = "INSERT INTO Cakes (id, name, price, description, size, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, cake.getName());
            statement.setInt(3, cake.getPrice());
            statement.setString(4, cake.getDescription());
            statement.setString(5, cake.getSize());
            statement.setInt(6, cake.getQuantity());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add cake: " + e.getMessage());
        }
    }

    @Override
    public void updateEntity(Integer id, Cake cake) {
        String sql = "UPDATE Cakes SET name = ?, price = ?, description = ?, size = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cake.getName());
            statement.setInt(2, cake.getPrice());
            statement.setString(3, cake.getDescription());
            statement.setString(4, cake.getSize());
            statement.setInt(5, cake.getQuantity());
            statement.setInt(6, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No cake found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update cake: " + e.getMessage());
        }
    }

    @Override
    public Optional<Cake> deleteEntity(Integer id) {
        String sql = "DELETE FROM Cakes WHERE id = ?";
        if(findEntity(id).isPresent())
        {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("No cake found with ID: " + id);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete cake: " + e.getMessage());
            }
            return findEntity(id);
        }
        return null;
    }

    @Override
    public Optional<Cake> findEntity(Integer id) {
        String sql = "SELECT * FROM Cakes WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Cake CaketoFind =  new Cake(
                        resultSet.getString("name"),
                        resultSet.getInt("price"),
                        resultSet.getString("description"),
                        resultSet.getString("size"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("id")
                );
                return Optional.of(CaketoFind);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find cake: " + e.getMessage());
        }
    }

    @Override
    public Iterator<Cake> findAllEntities() {
        List<Cake> cakes = new ArrayList<>();
        String sql = "SELECT * FROM Cakes";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Cake cake = new Cake(
                        resultSet.getString("name"),
                        resultSet.getInt("price"),
                        resultSet.getString("description"),
                        resultSet.getString("size"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("id")
                );
                cakes.add(cake);
            }
            return cakes.iterator();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all cakes: " + e.getMessage());
        }
    }

    // Helper method to close the connection when needed
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
