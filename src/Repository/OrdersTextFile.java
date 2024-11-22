package Repository;

import Domain.Order;
import Domain.Cake;
import Exceptions.FileNotValidException;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;

public class OrdersTextFile extends FileRepository<Integer, Order> {

    private final IRepository<Integer, Cake> cakesShopTextFile; // Reference to access cake details from CakeShop

    public OrdersTextFile(String fileName, IRepository<Integer, Cake> cakesShopTextFile) throws FileNotValidException {
        super();
        if (cakesShopTextFile == null) {
            throw new FileNotValidException("CakesShopTextFile reference cannot be null");
        }
        this.cakesShopTextFile = cakesShopTextFile;
        this.filename = fileName;
        readFile();
    }

    @Override
    public void readFile() throws FileNotValidException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length != 2) { // Assuming each line has order ID and cake ID
                    throw new FileNotValidException("File not valid!");
                } else {
                    int orderId = Integer.parseInt(tokens[0]);
                    int cakeId = Integer.parseInt(tokens[1]);
                    Optional<Cake> cake = cakesShopTextFile.findEntity(cakeId); // Retrieve cake object by ID
                    Cake cake1 = cake.orElseThrow(() -> new IllegalArgumentException("Cake not found"));
                    if (cake != null) {
                        Order order = new Order(orderId, cake1);
                        map.put(order.getId(), order);
                    } else {
                        System.out.println("Error: Cake ID " + cakeId + " not found in CakeShop.");
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {

            Iterator<Order> it = findAllEntities();
            while (it.hasNext()) {
                Order order = it.next();
                bw.write(order.getId() + "," + order.getCake().getId() + "\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
