package Service;

import Domain.Cake;
import Repository.*;
import Filter.FilterCakeByPrice;
import Filter.FilterCakeBySize;
import Repository.FilteredRepository;
import Exceptions.CakeExceptions;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CakeService extends CakeShop {
    IRepository<Integer, Cake> cakeShop;

    public CakeService(IRepository<Integer, Cake> cakeShop) {
        this.cakeShop = cakeShop;
    }

    public void add(Integer id, Cake cake) {
        if (cakeShop.findEntity(id).isPresent()) {
            throw new CakeExceptions("A cake with ID " + id + " already exists.");
        }

        if (id == null || cake.getName() == null || cake.getName().isEmpty() || cake.getDescription() == null ||
                cake.getDescription().isEmpty() || cake.getSize() == null || cake.getSize().isEmpty() ||
                !cake.getSize().matches("Small|Medium|Large") || cake.getPrice() < 0 || cake.getQuantity() < 0) {
            throw new CakeExceptions("Invalid cake attributes.");
        }

        cakeShop.addEntity(id, cake);
    }

    public Optional<Cake> remove(Integer id) {
        if (cakeShop.findEntity(id).isEmpty()) {
            throw new CakeExceptions("A cake with ID " + id + " does not exist.");
        }
        if (id == null) {
            throw new CakeExceptions("ID cannot be null or empty.");
        }
        return cakeShop.deleteEntity(id);
    }

    public void update(Integer id, Cake cake) {
        if (cakeShop.findEntity(id).isEmpty()) {
            throw new CakeExceptions("A cake with ID " + id + " does not exist.");
        }
        if (id == null || cake.getName() == null ||
                cake.getName().isEmpty() || cake.getDescription() == null || cake.getDescription().isEmpty() ||
                cake.getSize() == null || cake.getSize().isEmpty() || cake.getPrice() < 0 || cake.getQuantity() < 0) {
            throw new CakeExceptions("Invalid cake attributes.");
        }

        cakeShop.deleteEntity(id);
        cakeShop.addEntity(cake.getId(), cake);
    }

    public Optional<Cake> showCake(Integer id) {
        if (id == null || cakeShop.findEntity(id).isEmpty()) {
            throw new CakeExceptions("A cake with ID " + id + " does not exist.");
        }
        return cakeShop.findEntity(id);
    }

    public ArrayList<Cake> getCakeList() {
        ArrayList<Cake> cakeList = new ArrayList<>();
        cakeShop.findAllEntities().forEachRemaining(cakeList::add);

        if (cakeList.isEmpty()) {
            throw new CakeExceptions("No cakes found.");
        }

        return cakeList;
    }

    public ArrayList<Cake> getCakeListByPrice(int price) {
        Predicate<Cake> priceFilter = cake -> cake.getPrice() <= price;
        return getCakesByFilter(priceFilter);
    }


    public ArrayList<Cake> getCakeListBySize(String size) {
        Predicate<Cake> sizeFilter = cake -> cake.getSize().equalsIgnoreCase(size);
        return getCakesByFilter(sizeFilter);
    }

    public ArrayList<Cake> getCakesByFilter(Predicate<Cake> filterCondition) {
        ArrayList<Cake> filteredCakes = new ArrayList<>();
        getCakeList().stream()
                .filter(filterCondition)
                .forEach(filteredCakes::add);

        if (filteredCakes.isEmpty()) {
            throw new CakeExceptions("No cakes found.");
        }
        return filteredCakes;
    }




}
