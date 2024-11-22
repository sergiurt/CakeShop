package Service;

import Domain.Cake;
import Exceptions.CakeExceptions;
import Repository.CakeShop;
import Repository.Repository;
import Service.CakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CakeServiceTest {

    private CakeService cakeService;

    @BeforeEach
    public void setUp() {
        CakeShop cakeShop = new CakeShop();
        this.cakeService = new CakeService(cakeShop);
    }


    @Test
    public void testAddCake() {
        Cake cake = new Cake("Test Cake", 300, "Description", "Medium", 10, 1);
        cakeService.add(1, cake);

        assertEquals(Optional.of(cake), cakeService.showCake(1));
    }

    @Test
    public void testAddDuplicateCake() {
        Cake cake = new Cake("Test Cake", 300, "Description", "Medium", 10, 1);
        cakeService.add(1, cake);

        Cake duplicateCake = new Cake("Duplicate Cake", 350, "New Description", "Large", 5, 1);

        assertThrows(CakeExceptions.class, () -> cakeService.add(1, duplicateCake));
    }

    @Test
    public void testRemoveCake() {
        Cake cake = new Cake("Test Cake", 300, "Description", "Medium", 10, 1);
        cakeService.add(1, cake);

        cakeService.remove(1);

        assertThrows(CakeExceptions.class, () -> cakeService.showCake(1));
    }

    @Test
    public void testUpdateCake() {
        Cake cake = new Cake("Test Cake", 300, "Description", "Medium", 10, 1);
        cakeService.add(1, cake);

        Cake updatedCake = new Cake("Updated Cake", 350, "Updated Description", "Large", 5, 2);
        cakeService.update(1, updatedCake);

        assertEquals(Optional.of(updatedCake), cakeService.showCake(1));
    }

    @Test
    public void testGetCakeList() {
        Cake cake1 = new Cake("Cake 1", 300, "Description 1", "Small", 10, 1);
        Cake cake2 = new Cake("Cake 2", 350, "Description 2", "Large", 5, 2);

        cakeService.add(1, cake1);
        cakeService.add(2, cake2);

        ArrayList<Cake> cakes = cakeService.getCakeList();
        assertEquals(2, cakes.size());
        assertTrue(cakes.contains(cake1));
        assertTrue(cakes.contains(cake2));
    }

    @Test
    public void testGetCakeListByPrice() {
        Cake cake1 = new Cake("Cake 1", 300, "Description 1", "Small", 10, 1);
        Cake cake2 = new Cake("Cake 2", 350, "Description 2", "Large", 5, 2);

        cakeService.add(1, cake1);
        cakeService.add(2, cake2);

        ArrayList<Cake> cakes = cakeService.getCakeListByPrice(300);
        assertEquals(1, cakes.size());
        assertEquals(cake1, cakes.get(0));
    }

    @Test
    public void testGetCakeListBySize() {
        Cake cake1 = new Cake("Cake 1", 300, "Description 1", "Small", 10, 1);
        Cake cake2 = new Cake("Cake 2", 350, "Description 2", "Large", 5, 2);

        cakeService.add(1, cake1);
        cakeService.add(2, cake2);

        ArrayList<Cake> smallCakes = cakeService.getCakeListBySize("Small");
        assertEquals(1, smallCakes.size());
        assertEquals(cake1, smallCakes.get(0));
    }

    @Test
    public void testInvalidSizeInAdd() {
        Cake cake = new Cake("Invalid Size Cake", 300, "Description", "Extra Large", 10, 1);

        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));
    }

    @Test
    public void testNegativePriceInAdd() {
        Cake cake = new Cake("Negative Price Cake", -100, "Description", "Medium", 10, 1);

        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));
    }

    @Test
    public void testNegativeQuantityInAdd() {
        Cake cake = new Cake("Negative Quantity Cake", 300, "Description", "Medium", -5, 1);

        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));
    }

    @Test
    public void testShowNonexistentCakeThrowsException() {
        assertThrows(CakeExceptions.class, () -> cakeService.showCake(99));
    }

    @Test
    public void testRemoveNonexistentCakeThrowsException() {
        assertThrows(CakeExceptions.class, () -> cakeService.remove(99));
    }

    @Test
    public void testUpdateNonexistentCakeThrowsException() {
        Cake newCake = new Cake("New Cake", 400, "Description", "Medium", 5, 99);
        assertThrows(CakeExceptions.class, () -> cakeService.update(99, newCake));
    }

    @Test
    public void testGetCakeListByInvalidSizeThrowsException() {
        assertThrows(CakeExceptions.class, () -> cakeService.getCakeListBySize("Invalid Size"));
    }

    @Test
    public void testAddCakeWithNullNameThrowsException() {
        Cake cake = new Cake(null, 200, "Description", "Medium", 5, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));
    }

    @Test
    public void testAddCakeWithEmptyDescriptionThrowsException() {
        Cake cake = new Cake("Cake with Empty Description", 150, "", "Small", 3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));
    }

    @Test
    public void testUpdateCakeWithNullNameThrowsException() {
        Cake cake = new Cake("Original Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, cake);

        Cake updatedCake = new Cake(null, 200, "Updated Description", "Small", 5, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }

    @Test
    public void testUpdateCakeWithEmptyDescriptionThrowsException() {
        Cake cake = new Cake("Original Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, cake);

        Cake updatedCake = new Cake("Updated Cake", 200, "", "Small", 5, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }

    @Test
    public void testAddCakeWithNullId() {
        Cake cake = new Cake("Test Cake", 300, "Description", "Medium", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(null, cake));
    }

    // Test for null or empty name
    @Test
    public void testAddCakeWithNullOrEmptyName() {
        Cake cake = new Cake(null, 300, "Description", "Medium", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));

        Cake cakeWithEmptyName = new Cake("", 300, "Description", "Medium", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cakeWithEmptyName));
    }

    // Test for null or empty description
    @Test
    public void testAddCakeWithNullOrEmptyDescription() {
        Cake cake = new Cake("Test Cake", 300, null, "Medium", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));

        Cake cakeWithEmptyDescription = new Cake("Test Cake", 300, "", "Medium", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cakeWithEmptyDescription));
    }

    // Test for null or empty size
    @Test
    public void testAddCakeWithNullOrEmptySize() {
        Cake cake = new Cake("Test Cake", 300, "Description", null, 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cake));

        Cake cakeWithEmptySize = new Cake("Test Cake", 300, "Description", "", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.add(1, cakeWithEmptySize));
    }

    // Test for removing a non-existent cake
    @Test
    public void testRemoveNonExistentCake() {
        assertThrows(CakeExceptions.class, () -> cakeService.remove(99));
    }

    // Test for null ID when removing a cake
    @Test
    public void testRemoveCakeWithNullId() {
        assertThrows(CakeExceptions.class, () -> cakeService.remove(null));
    }

    // Test for updating a non-existent cake
    @Test
    public void testUpdateNonExistentCake() {
        Cake cake = new Cake("Updated Cake", 350, "Updated Description", "Large", 5, 2);
        assertThrows(CakeExceptions.class, () -> cakeService.update(99, cake));
    }

    // Test for null ID when updating a cake
    @Test
    public void testUpdateCakeWithNullId() {
        Cake cake = new Cake("Updated Cake", 350, "Updated Description", "Large", 5, 2);
        assertThrows(CakeExceptions.class, () -> cakeService.update(null, cake));
    }

    // Test for showing a non-existent cake
    @Test
    public void testShowNonExistentCake() {
        assertThrows(CakeExceptions.class, () -> cakeService.showCake(99));
    }

    // Test for null ID when showing a cake
    @Test
    public void testShowCakeWithNullId() {
        assertThrows(CakeExceptions.class, () -> cakeService.showCake(null));
    }

    // Test for empty cake list
    @Test
    public void testGetEmptyCakeList() {
        assertThrows(CakeExceptions.class, () -> cakeService.getCakeList());
    }


    // Test for empty filtered list by size
    @Test
    public void testGetCakeListBySizeNoMatches() {
        Cake cake = new Cake("Test Cake", 300, "Description", "Medium", 10, 1);
        cakeService.add(1, cake);
        assertThrows(CakeExceptions.class, () -> cakeService.getCakeListBySize("Extra Large"));
    }

    @Test
    public void testUpdateWithNullId() {
        Cake cake = new Cake("Valid Cake", 300, "Description", "Medium", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(null, cake));
    }

    @Test
    public void testUpdateWithNonExistentId() {
        Cake cake = new Cake("Valid Cake", 300, "Description", "Medium", 10, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(99, cake));
    }

    @Test
    public void testUpdateWithExistingCakeId() {
        Cake existingCake = new Cake("Existing Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, existingCake);

        Cake newCake = new Cake("New Cake", 250, "New Description", "Medium", 3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, newCake));
    }

    @Test
    public void testUpdateWithNullName() {
        Cake existingCake = new Cake("Existing Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, existingCake);

        Cake updatedCake = new Cake(null, 250, "New Description", "Medium", 3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }

    @Test
    public void testUpdateWithEmptyDescription() {
        Cake existingCake = new Cake("Existing Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, existingCake);

        Cake updatedCake = new Cake("Updated Cake", 250, "", "Medium", 3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }

    @Test
    public void testUpdateWithNullSize() {
        Cake existingCake = new Cake("Existing Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, existingCake);

        Cake updatedCake = new Cake("Updated Cake", 250, "New Description", null, 3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }

    @Test
    public void testUpdateWithInvalidSize() {
        Cake existingCake = new Cake("Existing Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, existingCake);

        Cake updatedCake = new Cake("Updated Cake", 250, "New Description", "Extra Large", 3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }

    @Test
    public void testUpdateWithNegativePrice() {
        Cake existingCake = new Cake("Existing Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, existingCake);

        Cake updatedCake = new Cake("Updated Cake", -100, "New Description", "Medium", 3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }

    @Test
    public void testUpdateWithNegativeQuantity() {
        Cake existingCake = new Cake("Existing Cake", 200, "Description", "Small", 5, 1);
        cakeService.add(1, existingCake);

        Cake updatedCake = new Cake("Updated Cake", 250, "New Description", "Medium", -3, 1);
        assertThrows(CakeExceptions.class, () -> cakeService.update(1, updatedCake));
    }




}
