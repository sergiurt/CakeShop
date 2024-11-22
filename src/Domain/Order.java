package Domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

public class Order implements Identifiable, Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Cake cake;

    public Order(int id, Cake cake) {
        this.id = id;
        this.cake = cake;
    }

    public Order() {}

    @Override
    public int getId(){
        return id;
    }

    @Override
    public void setId(int id){
        this.id = id;
    }

    public Cake getCake(){
        return cake;
    }

    public void setCake(Cake cake){
        this.cake = cake;
    }

    public int getQuantity(){
        return cake.getQuantity();
    }

    @Override
    public String toString() {
        return "Order with ID: " + id + " and cake " + cake.getId() + "." + cake.getName() + " " + cake.getPrice() + " " + cake.getDescription() + " " + cake.getSize() + " " + cake.getQuantity() + " ";
    }
}
