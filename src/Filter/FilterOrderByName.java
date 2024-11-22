package Filter;

import Domain.Order;

public class FilterOrderByName implements AbstractFilter<Order>{
    private String name;

    public FilterOrderByName(String name) {
        this.name = name;
    }

    public boolean accept(Order order) {

        return order.getCake().getName().equals(name);
    }

}
