package Filter;

import Domain.Cake;

public class FilterCakeByPrice implements AbstractFilter<Cake>{
    private int price;

    public FilterCakeByPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean accept(Cake cake) {
        return cake.getPrice() <= price;
    }
}
