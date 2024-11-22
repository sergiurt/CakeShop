package UI;

import Domain.Cake;
import Exceptions.CakeExceptions;
import Exceptions.OrderExceptions;
import Repository.*;
import Domain.Order;
import Exceptions.*;

import Service.CakeService;
import Service.OrderService;
import Service.ReportService;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.Properties;

public class UI {
    private CakeService cakeshop;
    private OrderService orders;
    private ReportService reports;

    public UI(CakeService cakeshop, OrderService orders, ReportService reports) {
        this.reports = new ReportService(orders, cakeshop);
        this.cakeshop = cakeshop;
        this.orders = orders;
    }

    public static void print_menu() {
        System.out.println("MENU");
        System.out.println("1: Display all cakes");
        System.out.println("2. Add a new cake to the shop");
        System.out.println("3: Update a cake from the shop");
        System.out.println("4: Remove a cake from the shop");
        System.out.println("5: Add an Order");
        System.out.println("6: Cancel an Order");
        System.out.println("7: Finish an Order");
        System.out.println("8: Verify an Order");
        System.out.println("9: Update an Order");
        System.out.println("10: Display all Orders");
        System.out.println("11: Display cakes filtered by price");
        System.out.println("12: Display cakes filtered by Size(Small/Medium/Large)");
        System.out.println("13: Display orders filtered by Name of the cake");
        System.out.println("14: Display orders filtered by how much pieces are left in the shop");
        System.out.println("15. Get Orders for a Cake");
        System.out.println("16. Revenue Per Size");
        System.out.println("17. Low Inventory Cakes");
        System.out.println("18. Most Expensive Cake Per Size");
        System.out.println("19. Order Count by Description");
        System.out.println("20: Exit program");
        System.out.print("Enter your selection: ");
    }

    void run()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to CakeShop!");
        /*
 Create cakes and add them to the shop
        Cake cake1 = new Cake("Chocolate Fudge Cake", 500, "Rich chocolate cake layered with creamy fudge frosting.", "Medium", 5, 1);
        Cake cake2 = new Cake("Red Velvet Cake", 450, "Moist red velvet layers with cream cheese frosting.", "Large", 7, 2);
        Cake cake3 = new Cake("Lemon Drizzle Cake", 400, "Zesty lemon cake topped with a sugary glaze.", "Small", 10, 3);
        Cake cake4 = new Cake("Carrot Cake", 400, "Moist carrot cake with walnuts and cream cheese frosting.", "Medium", 12, 4);
        Cake cake5 = new Cake("Vanilla Sponge Cake", 300, "Soft and fluffy vanilla cake with buttercream icing.", "Small", 6, 5);

        cakeshop.add(cake1.getId(), cake1);
        cakeshop.add(cake2.getId(), cake2);
        cakeshop.add(cake3.getId(), cake3);
        cakeshop.add(cake4.getId(), cake4);
        cakeshop.add(cake5.getId(), cake5);

        orders.add(1, new Order(1, cake1));
        orders.add(2, new Order(2, cake2));
        orders.add(3, new Order(3, cake2));
        orders.add(4, new Order(4, cake4));
        orders.add(5, new Order(5, cake3));
        orders.add(6, new Order(6, cake1));
        orders.add(7, new Order(7, cake2));
        orders.add(8, new Order(8, cake3));
        orders.add(9, new Order(9, cake4));
        orders.add(10, new Order(10, cake5));
*/


        int option = 0;

        do {
            print_menu();
            option = input.nextInt();
            input.nextLine();  // Consume leftover newline after nextInt()

            switch (option) {
                case 1:
                    // Display all cakes
                    ArrayList<Cake> cakes = cakeshop.getCakeList();
                    if (cakes.isEmpty()) {
                        System.out.println("No cakes in the shop.");
                    }
                    else
                    {
                        for(Cake c : cakes)
                            System.out.println(c.toString());
                    }

                    break;

                case 2:
                    // Add a new cake to the shop
                    boolean validInput = false; // Flag to track valid input
                    while (!validInput) {
                        try {
                            System.out.println("What cake would you like to add to the shop?");
                            System.out.println("Name:");
                            String name = input.nextLine();

                            System.out.println("Price:");
                            int price = input.nextInt();
                            input.nextLine();  // Consume leftover newline

                            System.out.println("Description:");
                            String description = input.nextLine();

                            System.out.println("Size:");
                            String size = input.nextLine();

                            System.out.println("Quantity:");
                            int quantity = input.nextInt();
                            input.nextLine();  // Consume leftover newline

                            System.out.println("Cake ID:");
                            int cakeID = input.nextInt();
                            input.nextLine();

                            cakeshop.add(cakeID, new Cake(name, price, description, size, quantity, cakeID));
                            System.out.println("Cake added to the shop.");
                            validInput = true; // Input was valid, exit loop

                        } catch (CakeExceptions e) {
                            System.out.println("Error adding cake: " + e.getMessage());
                            System.out.println("Please try again."); // Prompt to retry
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred: " + e.getMessage());
                            System.out.println("Please try again."); // Prompt to retry
                        }
                    }
                    break;



                case 3:
                    // Update a cake from the shop
                    boolean valid = false;
                    while (!valid) {
                        try{
                            System.out.println("What cake would you like to update?");
                            int id3 = input.nextInt();
                            input.nextLine();
                            Optional<Cake> cake = cakeshop.showCake(id3);
                            System.out.println("New Cake ID:");
                            int cakeID1 = input.nextInt();
                            input.nextLine();

                            System.out.println("Cake name:");
                            String cakeName = input.nextLine();

                            System.out.println("Price:");
                            int price1 = input.nextInt();
                            input.nextLine();  // Consume leftover newline

                            System.out.println("Description:");
                            String description1 = input.nextLine();

                            System.out.println("Size:");
                            String size1 = input.nextLine();

                            System.out.println("Quantity:");
                            int quantity1 = input.nextInt();
                            input.nextLine();  // Consume leftover newline


                            cakeshop.update(id3, new Cake(cakeName, price1, description1, size1, quantity1, cakeID1));
                            System.out.println("Cake updated.");
                            valid = true;
                        }catch (CakeExceptions e) {
                            System.out.println("Error updating cake: " + e.getMessage());
                        }
                    }

                    break;


                case 4:
                    // Remove a cake from the shop
                    boolean valid2 = false;
                    while (!valid2) {
                        try
                        {
                            System.out.println("What cake would you like to remove?");
                            int id2 = input.nextInt();
                            input.nextLine();
                            Optional<Cake> cake = cakeshop.showCake(id2);
                            Cake cake1 = cake.orElse(null);
                            for(Order o: orders.getOrders())
                            {
                                if(o.getCake().equals(cake1))
                                    orders.remove(o.getId());
                            }
                            System.out.println(cakeshop.remove(id2));
                            valid2 = true;
                        }catch (CakeExceptions e) {
                            System.out.println("Error deleting cake: " + e.getMessage());
                        }

                    }
                    break;


                case 5:
                    // Add an order
                    boolean valid3 = false;
                    while (!valid3) {
                        try{
                            System.out.println("What cake would you like to order?");
                            int id = input.nextInt();
                            input.nextLine();
                            Optional<Cake> cake6 = cakeshop.showCake(id);
                            System.out.println("Set the id for the order:");
                            int orderId = input.nextInt();
                            input.nextLine();
                            Order order = new Order(orderId, cake6.orElse(null));
                            orders.add(orderId, order);
                            System.out.println(cake6.get().toStringWName());
                            valid3 = true;
                        }catch (CakeExceptions e) {
                            System.out.println("Error ordering cake: " + e.getMessage());
                        }catch(OrderExceptions e) {
                            System.out.println("Error ordering cake: " + e.getMessage());
                        }
                    }

                    break;

                case 6:
                    // Cancel an order
                    boolean valid4 = false;
                    while (!valid4) {
                        try
                        {
                            System.out.println("What order would you like to cancel?");
                            int orderId2 = input.nextInt();
                            input.nextLine();
                            orders.remove(orderId2);
                            valid4 = true;
                        }catch (CakeExceptions e) {
                            System.out.println("Error cancelling order: " + e.getMessage());
                        }catch(OrderExceptions e) {
                            System.out.println("Error cancelling order " + e.getMessage());
                        }
                    }
                    break;


                case 7:
                    // Finish an order
                    boolean orderFinished = false;  // Flag to track success of finishing an order
                    while (!orderFinished) {
                        try {
                            System.out.println("What order would you like to finish? (Enter order ID)");
                            int orderId3 = input.nextInt();
                            input.nextLine(); // Consume leftover newline

                            // Get the order first
                            Optional<Order> orderToFinish = orders.showOrder(orderId3);

                            if (orderToFinish.isPresent()) {
                                Cake cakeToFinish = orderToFinish.get().getCake();
                                if (cakeToFinish.getQuantity() > 0) {
                                    // Reduce cake quantity
                                    cakeToFinish.setQuantity(cakeToFinish.getQuantity() - 1);
                                    // Remove the order after reducing the cake quantity
                                    orders.remove(orderId3);
                                    System.out.println("Order finished. Cake quantity updated.");
                                    orderFinished = true; // Set flag to true as operation was successful
                                } else {
                                    System.out.println("We don't have that cake available.");
                                    // Optionally, you can prompt to try again if the cake is not available
                                }
                            } else {
                                System.out.println("Order not found.");
                            }
                            orderFinished = true;
                        } catch (OrderExceptions e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;



                case 8:
                    // Verify an order
                    boolean valid5 = false;
                    while (!valid5) {
                        try
                        {
                            System.out.println("What order would you like to search for?");
                            int OrderId4 = input.nextInt();
                            input.nextLine();
                            System.out.println(orders.showOrder(OrderId4));
                            valid5 = true;
                        }catch (CakeExceptions e) {
                            System.out.println("Error: " + e.getMessage());
                        }catch(OrderExceptions e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }

                    break;


                case 9:
                    // Update an order
                    boolean validUpdate = false; // Flag for update validation
                    while (!validUpdate) {
                        try {
                            System.out.println("What order would you like to update?");
                            int orderId5 = input.nextInt();
                            input.nextLine(); // Consume leftover newline
                            Optional<Order> orderToUpdate = orders.showOrder(orderId5);

                            if (orderToUpdate != null) {
                                System.out.println("What is the new Cake you want to order?");
                                int orderId6 = input.nextInt();
                                input.nextLine(); // Consume leftover newline
                                Optional<Cake> cake7 = cakeshop.showCake(orderId6);
                                Cake cake = cake7.orElseThrow(() -> new IllegalArgumentException("Cake not found"));
                                Order order = new Order(orderId5, cake);

                                orders.update(orderId5, order);
                                System.out.println("Order updated successfully.");
                            } else {
                                System.out.println("Order not found.");
                            }
                            validUpdate = true; // Exit loop if successful
                        } catch (OrderExceptions e) {
                            System.out.println("Error updating order: " + e.getMessage());
                        } catch (CakeExceptions e) {
                            System.out.println("Error finding cake: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred: " + e.getMessage());
                        }
                    }
                    break;

                case 10:
                    // Display all orders
                    boolean validDisplayOrders = false; // Flag for order display
                    while (!validDisplayOrders) {
                        try {
                            ArrayList<Order> orders1 = orders.getOrders();
                            if (orders1.isEmpty()) {
                                System.out.println("No orders found.");
                            } else {
                                for (Order o : orders1) {
                                    System.out.println(o.toString());
                                }
                            }
                            validDisplayOrders = true; // Exit loop after displaying
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred while displaying orders: " + e.getMessage());
                        }
                    }
                    break;

                case 11:
                    // Display cakes filtered by price
                    boolean validFilterPrice = false; // Flag for filtering by price
                    while (!validFilterPrice) {
                        try {
                            System.out.println("What's your budget for the cake?");
                            int filterPrice = input.nextInt();
                            input.nextLine(); // Consume leftover newline

                            ArrayList<Cake> cakesFilteredByPrice = cakeshop.getCakeListByPrice(filterPrice);
                            if (cakesFilteredByPrice.isEmpty()) {
                                System.out.println("No cakes in your budget.");
                            } else {
                                for (Cake c : cakesFilteredByPrice) {
                                    System.out.println(c.toString());
                                }
                                System.out.println("These are the cakes within your budget.");
                            }
                            validFilterPrice = true; // Exit loop if successful
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred while filtering cakes by price: " + e.getMessage());
                        }
                    }
                    break;

                case 12:
                    // Display cakes filtered by size
                    boolean validFilterSize = false; // Flag for filtering by size
                    while (!validFilterSize) {
                        try {
                            System.out.println("What size are you looking for in the cake?");
                            String filterSize = input.nextLine();

                            ArrayList<Cake> cakesFilteredBySize = cakeshop.getCakeListBySize(filterSize);
                            if (cakesFilteredBySize.isEmpty()) {
                                System.out.println("No cakes of this size available.");
                            } else {
                                for (Cake c : cakesFilteredBySize) {
                                    System.out.println(c.toString());
                                }
                                System.out.println("These are the cakes of the specified size.");
                            }
                            validFilterSize = true; // Exit loop if successful
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred while filtering cakes by size: " + e.getMessage());
                        }
                    }
                    break;

                case 13:
                    // Display orders filtered by the name of the cake
                    boolean validFilterName = false; // Flag for filtering by cake name
                    while (!validFilterName) {
                        try {
                            System.out.println("What's the name of the cake you are looking for in the orders?");
                            String filterName = input.nextLine();

                            ArrayList<Order> ordersFilteredByName = orders.getOrdersByName(filterName);
                            if (ordersFilteredByName.isEmpty()) {
                                System.out.println("No orders for this cake.");
                            } else {
                                for (Order o : ordersFilteredByName) {
                                    System.out.println(o.toString());
                                }
                                System.out.println("These are the orders for the specified cake.");
                            }
                            validFilterName = true; // Exit loop if successful
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred while filtering orders by cake name: " + e.getMessage());
                        }
                    }
                    break;

                case 14:
                    // Display orders filtered by how many of this cake are left in the store
                    boolean validFilterQuantity = false; // Flag for filtering by quantity
                    while (!validFilterQuantity) {
                        try {
                            System.out.println("Enter the quantity for filtering the orders:");
                            int filterQuantity = input.nextInt();
                            input.nextLine(); // Consume leftover newline

                            ArrayList<Order> ordersFilteredByQuantity = orders.getOrdersByQuantity(filterQuantity);
                            if (ordersFilteredByQuantity.isEmpty()) {
                                System.out.println("No orders for cakes with such a large quantity left in stock.");
                            } else {
                                for (Order o : ordersFilteredByQuantity) {
                                    System.out.println(o.toString());
                                }
                                System.out.println("These are the orders for the cakes with a quantity equal to or greater than the given quantity.");
                            }
                            validFilterQuantity = true; // Exit loop if successful
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred while filtering orders by quantity: " + e.getMessage());
                        }
                    }
                    break;

                case 15: // Report 1: Get Orders for a Specific Cake
                    System.out.print("Enter cake name: ");
                    String cakeName = input.nextLine();
                    List<Order> orders = reports.getOrdersForCake(cakeName);
                    if (orders.isEmpty()) {
                        System.out.println("No orders found for the cake: " + cakeName);
                    } else {
                        System.out.println("Orders for cake '" + cakeName + "':");
                        orders.forEach(order -> System.out.println(order));
                    }
                    break;

                case 16: // Report 2: Revenue Per Cake Size
                    Map<String, Integer> revenuePerSize = reports.getRevenuePerSize();
                    if (revenuePerSize.isEmpty()) {
                        System.out.println("No revenue data available.");
                    } else {
                        System.out.println("Revenue per cake size:");
                        revenuePerSize.forEach((size, revenue) ->
                                System.out.println("Size: " + size + ", Revenue: " + revenue));
                    }
                    break;

                case 17: // Report 3: Low Inventory Cakes
                    System.out.print("Enter inventory threshold: ");
                    int threshold = input.nextInt();
                    List<Cake> lowInventoryCakes = reports.getLowInventoryCakes(threshold);
                    if (lowInventoryCakes.isEmpty()) {
                        System.out.println("All cakes have sufficient inventory.");
                    } else {
                        System.out.println("Cakes with inventory less than " + threshold + ":");
                        lowInventoryCakes.forEach(cake -> System.out.println(cake));
                    }
                    break;

                case 18: // Report 4: Most Expensive Cake Per Size
                    Map<String, Cake> expensiveCakes = reports.getMostExpensiveCakePerSize();
                    if (expensiveCakes.isEmpty()) {
                        System.out.println("No data available for expensive cakes.");
                    } else {
                        System.out.println("Most expensive cakes by size:");
                        expensiveCakes.forEach((size, cake) -> {
                            System.out.println("Size: " + size);
                            System.out.println("  " + cake);
                        });
                    }
                    break;

                case 19: // Report 5: Order Count by Cake Description
                    Map<String, Long> orderCountByDescription = reports.getOrderCountByDescription();
                    if (orderCountByDescription.isEmpty()) {
                        System.out.println("No orders available for analysis.");
                    } else {
                        System.out.println("Order counts by cake description:");
                        orderCountByDescription.forEach((description, count) ->
                                System.out.println("Description: " + description + ", Orders: " + count));
                    }
                    break;


                case 20:
                    // Exit program
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != 20);

        input.close();

    }

    public static void main(String[] args) {
        IRepository<Integer, Cake> cakeShop = null;
        IRepository<Integer, Order> orders = null;
        Properties prop = new Properties();

        try {
            // Load properties
            prop.load(new FileReader("settings.properties"));
            String repoType = prop.getProperty("RepoType");
            String CakesPath = prop.getProperty("CakesPath");
            String OrdersPath = prop.getProperty("OrdersPath");
            String DBURL = prop.getProperty("DBURL"); // Add DB URL from properties

            System.out.println("Repository Type: " + repoType);
            System.out.println("Cakes Path: " + CakesPath);
            System.out.println("Orders Path: " + OrdersPath);

            if ("text".equals(repoType)) {
                try {
                    System.out.println("Initializing CakesShopTextFile...");
                    cakeShop = new CakesShopTextFile(CakesPath);
                    System.out.println("CakesShopTextFile initialized successfully");

                    System.out.println("Initializing OrdersTextFile...");
                    orders = new OrdersTextFile(OrdersPath, cakeShop);
                    System.out.println("OrdersTextFile initialized successfully");

                } catch (FileNotValidException e) {
                    System.err.println("Error initializing repositories: " + e.getMessage());
                    throw e;
                }
            } else if ("inmemory".equals(repoType)) {
                cakeShop = new CakeShop();
                orders = new Orders();
            } else if ("binary".equals(repoType)) {
                cakeShop = new CakesShopBinaryFile(CakesPath);
                orders = new OrdersBinaryFile(OrdersPath);
            } else if ("database".equals(repoType)) {
                try {
                    System.out.println("Initializing DBRepository...");
                    cakeShop = new CakesDB(DBURL);
                    System.out.println("DBRepository for cakes initialized successfully");

                    orders = new OrdersDB(DBURL, cakeShop);
                    System.out.println("DBRepository for orders initialized successfully");

                } catch (RuntimeException e) {
                    System.err.println("Database connection error: " + e.getMessage());
                    throw new RuntimeException("Database connection error", e);
                }
            } else if ("xml".equals(repoType)) {
                System.out.println("Initializing CakesXMLFile...");
                cakeShop = new CakesXMLFile(CakesPath);
                System.out.println("CakesXMLFile initialized successfully");

                System.out.println("Initializing OrdersXMLFile...");
                orders = new OrdersXMLFile(OrdersPath, cakeShop);
                System.out.println("OrdersXMLFile initialized successfully");
            } else if ("json".equals(repoType)) {
                System.out.println("Initializing CakesJSONFile...");
                cakeShop = new CakesJSONFile(CakesPath);
                System.out.println("CakesJSONFile initialized successfully");

                System.out.println("Initializing OrdersJSONFile...");
                orders = new OrdersJSONFile(OrdersPath, cakeShop);
                System.out.println("OrdersJSONFile initialized successfully");

            }

            if (cakeShop == null || orders == null) {
                throw new RuntimeException("Failed to initialize repositories");
            }

            CakeService cakeShopService = new CakeService(cakeShop);
            OrderService orderService = new OrderService(orders, cakeShopService);
            ReportService reportService = new ReportService(orderService, cakeShopService);
            UI ui = new UI(cakeShopService, orderService, reportService);
            ui.run();

        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (FileNotValidException e) {
            System.err.println("File validation error: " + e.getMessage());
            throw new RuntimeException("File validation error", e);
        } catch (RuntimeException e) {
            System.err.println("Runtime error: " + e.getMessage());
            throw e;
        }


    }

}
