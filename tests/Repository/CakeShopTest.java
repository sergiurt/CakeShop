package Repository;

import Domain.Cake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CakeShopTest {
    private CakeShop cakeShop;
    private Cake cake1;
    private Cake cake2;

    @BeforeEach
    void setUp() {
        cakeShop = new CakeShop();
        cake1 = new Cake("Chocolate Cake", 20, "Rich chocolate", "Medium", 5, 1);
        cake2 = new Cake("Vanilla Cake", 15, "Vanilla and cream", "Small", 3, 2);
    }

    @Test
    void testAddEntity() {
        cakeShop.addEntity(cake1.getId(), cake1);
        assertEquals(Optional.of(cake1), cakeShop.findEntity(cake1.getId()));

        cakeShop.addEntity(cake2.getId(), cake2);
        assertEquals(Optional.of(cake2), cakeShop.findEntity(cake2.getId()));
    }

    @Test
    void testDeleteEntity() {
        cakeShop.addEntity(cake1.getId(), cake1);
        cakeShop.addEntity(cake2.getId(), cake2);

        cakeShop.deleteEntity(cake1.getId());

        // Check that cake1 is deleted
        assertTrue(cakeShop.findEntity(cake1.getId()).isEmpty());

        // Check that cake2 still exists
        assertTrue(cakeShop.findEntity(cake2.getId()).isPresent());
    }

    @Test
    void testUpdateEntity() {
        cakeShop.addEntity(cake1.getId(), cake1);

        Cake updatedCake = new Cake("Chocolate Fudge Cake", 25, "Extra fudge", "Medium", 3, 1);
        cakeShop.updateEntity(cake1.getId(), updatedCake);

        Optional<Cake> retrievedCake = cakeShop.findEntity(cake1.getId());
        Cake retrievedCake1 = Optional.ofNullable(retrievedCake.get()).orElse(cake1);
        assertEquals(updatedCake.getName(), retrievedCake1.getName());
        assertEquals(updatedCake.getPrice(), retrievedCake1.getPrice());
        assertEquals(updatedCake.getDescription(), retrievedCake1.getDescription());
    }

    @Test
    void testFindEntity() {
        cakeShop.addEntity(cake1.getId(), cake1);
        Optional<Cake> retrievedCake = cakeShop.findEntity(cake1.getId());
        Cake retrievedCake1 = Optional.ofNullable(retrievedCake.get()).orElse(cake1);
        assertNotNull(retrievedCake1);
        assertEquals(cake1, retrievedCake1);
    }

    @Test
    void testFindAllEntities() {
        cakeShop.addEntity(cake1.getId(), cake1);
        cakeShop.addEntity(cake2.getId(), cake2);

        Iterator<Cake> iterator = cakeShop.findAllEntities();
        assertTrue(iterator.hasNext());
        assertEquals(cake1, iterator.next());
        assertEquals(cake2, iterator.next());
    }
}
