package Filter;


import Domain.Cake;

public class FilterCakeBySize implements AbstractFilter<Cake> {
    private String size;

    public FilterCakeBySize(String size) {
        this.size = size;
    }

    @Override
    public boolean accept(Cake cake) {
        return cake.getSize().equals(this.size);
    }
}
