package Service;

import Domain.Cake;
import Domain.Order;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    private final OrderService orderService;
    private final CakeService cakeService;

    public ReportService(OrderService orderService, CakeService cakeService) {
        this.orderService = orderService;
        this.cakeService = cakeService;
    }


    public List<Order> getOrdersForCake(String cakeName) {
        return orderService.getOrders().stream()
                .filter(order -> order.getCake().getName().equalsIgnoreCase(cakeName))
                .collect(Collectors.toList());
    }


    public Map<String, Integer> getRevenuePerSize() {
        return orderService.getOrders().stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCake().getSize(),
                        Collectors.summingInt(order -> order.getCake().getPrice())
                ));
    }


    public List<Cake> getLowInventoryCakes(int threshold) {
        return cakeService.getCakeList().stream()
                .filter(cake -> cake.getQuantity() < threshold)
                .collect(Collectors.toList());
    }


    public Map<String, Cake> getMostExpensiveCakePerSize() {
        return cakeService.getCakeList().stream()
                .collect(Collectors.groupingBy(
                        Cake::getSize,
                        Collectors.collectingAndThen(
                                Collectors.maxBy((c1, c2) -> Integer.compare(c1.getPrice(), c2.getPrice())),
                                optional -> optional.orElse(null)
                        )
                ));
    }

    public Map<String, Long> getOrderCountByDescription() {
        return orderService.getOrders().stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCake().getDescription(),
                        Collectors.counting()
                ));
    }

}