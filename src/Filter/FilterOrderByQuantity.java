package Filter;


import Domain.Order;

public class FilterOrderByQuantity implements AbstractFilter<Order>{
    private int quantity;

    public FilterOrderByQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean accept(Order order) {
        return order.getCake().getQuantity() >= quantity;
    }
}
