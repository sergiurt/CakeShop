package Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CakeTest {
    private Cake cake1;
    private Cake cake2;

    @BeforeEach
    void setUp() {
        cake1 = new Cake("Chocolate Cake", 20, "Rich chocolate", "Medium", 5, 1);
        cake2 = new Cake("Vanilla Cake", 15, "Vanilla and cream", "Small", 3, 2);
    }

    @Test
    void testConstructor() {
        assertEquals("Chocolate Cake", cake1.getName());
        assertEquals(20, cake1.getPrice());
        assertEquals("Rich chocolate", cake1.getDescription());
        assertEquals("Medium", cake1.getSize());
        assertEquals(5, cake1.getQuantity());
        assertEquals(1, cake1.getId());
    }

    @Test
    void testSettersAndGetters() {
        cake1.setName("Strawberry Cake");
        cake1.setPrice(25);
        cake1.setDescription("Fresh strawberries");
        cake1.setSize("Large");
        cake1.setQuantity(10);
        cake1.setId(3);

        assertEquals("Strawberry Cake", cake1.getName());
        assertEquals(25, cake1.getPrice());
        assertEquals("Fresh strawberries", cake1.getDescription());
        assertEquals("Large", cake1.getSize());
        assertEquals(10, cake1.getQuantity());
        assertEquals(3, cake1.getId());
    }

    @Test
    void testToString() {
        String expected = "1.Chocolate Cake 20 Rich chocolate Medium 5 ";
        assertEquals(expected, cake1.toString());
    }

    @Test
    void testToStringWName() {
        String expected = "The cake 1 costs 20 and is made with Rich chocolate, the size is Medium we have 5 left.";
        assertEquals(expected, cake1.toStringWName());
    }

    @Test
    void testEquals() {
        Cake cake3 = new Cake("Chocolate Cake", 20, "Rich chocolate", "Medium", 5, 1);
        assertTrue(cake1.equals(cake3));
        assertFalse(cake1.equals(cake2));
    }
}
