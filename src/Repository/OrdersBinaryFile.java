package Repository;

import Domain.Order;
import Exceptions.FileNotValidException;

import java.io.*;
import java.util.HashMap;

public class OrdersBinaryFile extends FileRepository<Integer, Order> {

    public OrdersBinaryFile(String filename) throws FileNotValidException {
        super(filename);
    }

    @Override
    protected void readFile() throws FileNotValidException {

        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Orders binary file is missing or empty. Initializing empty orders.");
            this.map = new HashMap<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.map = (HashMap<Integer, Order>) ois.readObject();
        } catch (EOFException e) {
            System.out.println("Reached end of orders binary file.");
            this.map = new HashMap<>();
        } catch (IOException e) {
            System.out.println("Error reading orders binary file. Creating new empty file.");
            this.map = new HashMap<>();
            writeFile(); // Create new empty file
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Data format error in orders file", e);
        }
    }

    @Override
    protected void writeFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.filename))) {
            oos.writeObject(this.map);
        } catch (IOException e) {
            throw new RuntimeException("Error writing binary file", e);
        }
    }
}
