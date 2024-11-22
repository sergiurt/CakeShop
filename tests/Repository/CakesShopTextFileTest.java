package Repository;

import Domain.Cake;
import Exceptions.FileNotValidException;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CakesShopTextFileTest {
    private static final String TEST_FILENAME = "data/test_cakeshop.txt";
    private CakesShopTextFile cakesShopTextFile;

    @BeforeEach
    void setUp() throws FileNotValidException, IOException {
        // Ensure the test file exists before initializing CakesShopTextFile
        File testFile = new File(TEST_FILENAME);
        if (!testFile.exists()) {
            testFile.createNewFile();  // Creates an empty file
        }
        cakesShopTextFile = new CakesShopTextFile(TEST_FILENAME);
    }


    @AfterEach
    void tearDown() {
        // Delete the test file after each test
        File testFile = new File(TEST_FILENAME);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testReadFileWithNoData() {
        // Test reading from an empty file (no data written)
        File file = new File(TEST_FILENAME);
        try {
            cakesShopTextFile.readFile();
            assertEquals(0, countIterator(cakesShopTextFile.findAllEntities()));
        } catch (FileNotValidException e) {
            fail("File should be valid even if empty.");
        }
    }

    @Test
    void testReadFileWithData() throws IOException, FileNotValidException {
        // Prepare a sample file with cake data
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEST_FILENAME))) {
            bw.write("Test Cake,100,Description,Small,2,1\n");
            bw.write("Sample Cake,200,Sample Description,Medium,5,2\n");
        }

        // Read from the file and verify data
        cakesShopTextFile.readFile();
        assertEquals(2, countIterator(cakesShopTextFile.findAllEntities()));

        Optional<Cake> cake = cakesShopTextFile.findEntity(1);
        Cake cake1 = Optional.ofNullable(cake.get()).orElseThrow();
        assertNotNull(cake);
        assertEquals("Test Cake", cake1.getName());
    }

    @Test
    void testWriteFile() throws FileNotValidException {
        // Add cakes to the repository
        Cake cake1 = new Cake("Write Cake", 150, "Write test description", "Medium", 3, 10);
        Cake cake2 = new Cake("Another Cake", 200, "Another description", "Large", 2, 11);
        cakesShopTextFile.addEntity(cake1.getId(), cake1);
        cakesShopTextFile.addEntity(cake2.getId(), cake2);

        // Write to file
        cakesShopTextFile.writeFile();

        // Verify that the file contains the correct data
        try (BufferedReader br = new BufferedReader(new FileReader(TEST_FILENAME))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            assertEquals("Write Cake,150,Write test description,Medium,3,10", line1);
            assertEquals("Another Cake,200,Another description,Large,2,11", line2);
        } catch (IOException e) {
            fail("File reading failed: " + e.getMessage());
        }
    }

    @Test
    void testInvalidFileFormat() {
        // Write an invalid line to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEST_FILENAME))) {
            bw.write("Invalid line with wrong format");
        } catch (IOException e) {
            fail("File writing failed: " + e.getMessage());
        }

        // Reading the file should throw a FileNotValidException
        assertThrows(FileNotValidException.class, () -> cakesShopTextFile.readFile());
    }

    // Helper method to count entities in an iterator
    private int countIterator(Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}
