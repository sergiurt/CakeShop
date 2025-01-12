// BirthdayCakeBulkUpdateServiceTest.java
package Service;

import Domain.Cake;
import java.util.List;

public class BirthdayCakeBulkUpdateServiceTest {
    public static void main(String[] args) {
        BirthdayCakeBulkUpdateService service = new BirthdayCakeBulkUpdateService();

        System.out.println("Generating test cakes...");
        List<Cake> cakes = service.generateCakes(100_000);
        for(int i = 102; i<113;i++)
            System.out.println(cakes.get(i));
        System.out.println("Generated " + cakes.size() + " cakes");

        System.out.println("\nPerforming bulk update with traditional threads...");
        long threadsTime = measureExecutionTime(() -> service.updatePricesWithThreads(cakes, 4));
        for(int i = 102; i<113;i++)
            System.out.println(cakes.get(i));
        System.out.println("Traditional threads execution time: " + threadsTime + "ms");

        System.out.println("\nPerforming bulk update with ExecutorService...");
        long executorTime = measureExecutionTime(() -> service.updatePricesWithExecutor(cakes, 4));
        for(int i = 102; i<113;i++)
            System.out.println(cakes.get(i));
        System.out.println("ExecutorService execution time: " + executorTime + "ms");

        System.out.println("\nSorting cakes by price using multi-threading...");
        long sortTime = measureExecutionTime(() -> service.sortCakes(cakes, 4));
        System.out.println("Sorting execution time: " + sortTime + "ms");
        for(int i = 0; i<10000;i++)
            System.out.println(cakes.get(i));
        System.out.println("First cake after sorting: " + cakes.get(0));
        System.out.println("Last cake after sorting: " + cakes.get(cakes.size() - 1));
    }

    private static long measureExecutionTime(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - startTime;
    }
}
