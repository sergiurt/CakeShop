package Repository;

import Domain.Cake;
import Exceptions.FileNotValidException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CakesShopBinaryFileTest {
    private static final String TEST_FILENAME = "data/test_cakeshop.bin";
    private CakesShopBinaryFile cakesShopBinaryFile;

    private int countIterator(Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    @BeforeEach
    void setUp() throws FileNotValidException {
        cakesShopBinaryFile = new CakesShopBinaryFile(TEST_FILENAME);
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
    void testInitializeSampleData() {
        assertNotNull(cakesShopBinaryFile.findEntity(1));
        assertEquals(5, countIterator(cakesShopBinaryFile.findAllEntities()));
    }

    @Test
    void testReadFileWithNoData() throws FileNotValidException {
        File file = new File(TEST_FILENAME);
        if (file.exists()) {
            file.delete();
        }

        // Reading an empty file should initialize with sample data
        cakesShopBinaryFile.readFile();
        assertEquals(5, countIterator(cakesShopBinaryFile.findAllEntities()));
    }

    @Test
    void testReadFileWithData() throws FileNotValidException {
        Cake cake = new Cake("Test Cake", 100, "Test description", "Small", 1, 10);
        cakesShopBinaryFile.addEntity(10, cake);
        cakesShopBinaryFile.writeFile();

        CakesShopBinaryFile newFileInstance = new CakesShopBinaryFile(TEST_FILENAME);
        newFileInstance.readFile();

        Optional<Cake> retrievedCake = newFileInstance.findEntity(10);
        Cake retrievedCake1 = Optional.ofNullable(retrievedCake.get()).orElse(null);
        assertNotNull(retrievedCake);
        assertEquals("Test Cake", retrievedCake1.getName());
    }

    @Test
    void testWriteFile() {
        Cake cake = new Cake("Test Cake", 100, "Test description", "Small", 1, 10);
        cakesShopBinaryFile.addEntity(10, cake);
        cakesShopBinaryFile.writeFile();

        // Read the file back and confirm it saved correctly
        CakesShopBinaryFile readBackShop;
        try {
            readBackShop = new CakesShopBinaryFile(TEST_FILENAME);
            readBackShop.readFile();
            Optional<Cake> retrievedCake = readBackShop.findEntity(10);
            Cake retrievedCake1 = Optional.ofNullable(retrievedCake.get()).orElse(null);
            assertNotNull(retrievedCake);
            assertEquals("Test Cake", retrievedCake1.getName());
        } catch (FileNotValidException e) {
            fail("Exception should not be thrown when reading back the file: " + e.getMessage());
        }
    }

    @Test
    void testAddEntity() {
        Cake newCake = new Cake("New Cake", 200, "New description", "Medium", 5, 6);
        cakesShopBinaryFile.addEntity(newCake.getId(), newCake);

        Optional<Cake> retrievedCake = cakesShopBinaryFile.findEntity(newCake.getId());
        Cake retrievedCake1 = Optional.ofNullable(retrievedCake.get()).orElse(null);
        assertNotNull(retrievedCake);
        assertEquals(newCake.getName(), retrievedCake1.getName());
    }

    @Test
    void testDeleteEntity() {
        Cake cake = new Cake("Delete Cake", 300, "For deletion", "Large", 2, 7);
        cakesShopBinaryFile.addEntity(cake.getId(), cake);
        assertNotNull(cakesShopBinaryFile.findEntity(cake.getId()));

        cakesShopBinaryFile.deleteEntity(cake.getId());
        assertTrue(cakesShopBinaryFile.findEntity(cake.getId()).isEmpty());
    }

    @Test
    void testUpdateEntity() {
        Cake originalCake = new Cake("Original Cake", 150, "Original description", "Small", 4, 8);
        cakesShopBinaryFile.addEntity(originalCake.getId(), originalCake);

        Cake updatedCake = new Cake("Updated Cake", 180, "Updated description", "Medium", 6, 8);
        cakesShopBinaryFile.updateEntity(updatedCake.getId(), updatedCake);

        Optional<Cake> retrievedCake = cakesShopBinaryFile.findEntity(8);
        Cake retrievedCake1 = Optional.ofNullable(retrievedCake.get()).orElse(null);
        assertEquals("Updated Cake", retrievedCake1.getName());
        assertEquals(180, retrievedCake1.getPrice());
    }
}
