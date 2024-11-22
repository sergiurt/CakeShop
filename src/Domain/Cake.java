package Domain;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

public class Cake implements Identifiable, Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int price;
    private String description;
    private String size;
    private int quantity;
    private int id;

    public Cake(String name, int price, String description, String size, int quantity, int id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.size = size;
        this.quantity = quantity;
        this.id = id;
    }

    public Cake() {}

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return id + "." + name + " " + price + " " + description + " " + size + " " + quantity + " ";
    }
    public String toStringWName() {
        return "The cake " + id + " costs " + price + " and is made with " + description + ", the size is " + size + " we have " + quantity + " left.";
    }
    public boolean equals(Cake c) {
        return name.equals(c.name) && price == c.price && description.equals(c.description) && size.equals(c.size);
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
    public String getSize() {
        return size;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSize(String size) {
        this.size = size;
    }

}
