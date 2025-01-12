package Service;

import Domain.Cake;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BirthdayCakeBulkUpdateService {

    public List<Cake> generateCakes(int count) {
        List<Cake> cakes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = i % 2 == 0 ? "Vanilla" : "Chocolate";
            int price = 50 + (i % 100); // Prices vary between 50 and 149
            String description = "Delicious " + name + " cake";
            String size = (i % 3 == 0) ? "Small" : (i % 3 == 1) ? "Medium" : "Large";
            int quantity = 10 + (i % 50);
            int id = i + 1;
            cakes.add(new Cake(name, price, description, size, quantity, id));
        }
        return cakes;
    }

    public void updatePricesWithThreads(List<Cake> cakes, int numThreads) {
        int batchSize = cakes.size() / numThreads;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int startIndex = i * batchSize;
            final int endIndex = (i == numThreads - 1) ? cakes.size() : (i + 1) * batchSize;

            Thread thread = new Thread(() -> updateCakeBatch(cakes.subList(startIndex, endIndex)));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }
        }
    }

    public void updatePricesWithExecutor(List<Cake> cakes, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int batchSize = cakes.size() / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int startIndex = i * batchSize;
            final int endIndex = (i == numThreads - 1) ? cakes.size() : (i + 1) * batchSize;

            executor.submit(() -> updateCakeBatch(cakes.subList(startIndex, endIndex)));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void updateCakeBatch(List<Cake> batch) {
        batch.stream()
                .filter(cake -> "Vanilla".equals(cake.getName()))
                .forEach(cake -> cake.setPrice((int) (cake.getPrice() * 1.2)));
    }

    public void sortCakes(List<Cake> cakes, int numThreads) {
        if (cakes.size() <= 1) {
            return;
        }

        int batchSize = cakes.size() / numThreads;
        List<List<Cake>> partitions = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int start = i * batchSize;
            int end = (i == numThreads - 1) ? cakes.size() : (i + 1) * batchSize;
            partitions.add(new ArrayList<>(cakes.subList(start, end)));
        }

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Sort each partition concurrently
        for (List<Cake> partition : partitions) {
            executor.submit(() -> Collections.sort(partition, (c1, c2) -> Integer.compare(c1.getPrice(), c2.getPrice())));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Merge sorted partitions
        mergePartitions(cakes, partitions);
    }

    private void mergePartitions(List<Cake> cakes, List<List<Cake>> partitions) {
        cakes.clear();
        List<Cake> merged = partitions.get(0);

        for (int i = 1; i < partitions.size(); i++) {
            merged = mergeTwoLists(merged, partitions.get(i));
        }

        cakes.addAll(merged);
    }

    private List<Cake> mergeTwoLists(List<Cake> left, List<Cake> right) {
        List<Cake> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getPrice() <= right.get(j).getPrice()) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i++));
        }
        while (j < right.size()) {
            merged.add(right.get(j++));
        }

        return merged;
    }
    }
