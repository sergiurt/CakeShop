package Repository;

import Domain.Cake;
import Exceptions.FileNotValidException;

import java.io.*;
import java.util.HashMap;

public class CakesShopBinaryFile extends FileRepository<Integer, Cake> {

    public CakesShopBinaryFile(String filename) throws FileNotValidException {
        super(filename);
    }

    @Override
    public void readFile() throws FileNotValidException {
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Cakes binary file is missing or empty. Initializing with sample data.");
            this.map = new HashMap<>();
            initializeSampleData();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.map = (HashMap<Integer, Cake>) ois.readObject();
            if (this.map.isEmpty()) {
                System.out.println("Cakes file was empty. Initializing with sample data.");
                initializeSampleData();
            }
        } catch (EOFException e) {
            System.out.println("Reached end of cakes binary file. Initializing with sample data.");
            this.map = new HashMap<>();
            initializeSampleData();
        } catch (IOException e) {
            System.out.println("Error reading cakes binary file. Creating new file with sample data.");
            this.map = new HashMap<>();
            initializeSampleData();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Data format error in cakes file", e);
        }
    }

    private void initializeSampleData() {
        Cake[] sampleCakes = {
                new Cake("Chocolate Fudge Cake", 500, "Rich chocolate cake layered with creamy fudge frosting.", "Medium", 5, 1),
                new Cake("Red Velvet Cake", 450, "Moist red velvet layers with cream cheese frosting.", "Large", 7, 2),
                new Cake("Lemon Drizzle Cake", 400, "Zesty lemon cake topped with a sugary glaze.", "Small", 10, 3),
                new Cake("Carrot Cake", 400, "Moist carrot cake with walnuts and cream cheese frosting.", "Medium", 12, 4),
                new Cake("Vanilla Sponge Cake", 300, "Soft and fluffy vanilla cake with buttercream icing.", "Small", 6, 5)
        };

        for (Cake cake : sampleCakes) {
            addEntity(cake.getId(), cake);
        }

        try {
            writeFile();
            System.out.println("Sample cake data has been initialized and saved.");
        } catch (Exception e) {
            System.out.println("Warning: Could not save initial sample data: " + e.getMessage());
        }
    }

    @Override
    public void writeFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.filename))) {
            oos.writeObject(this.map);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to cakes binary file", e);
        }
    }
}