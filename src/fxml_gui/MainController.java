package fxml_gui;

import Domain.Order;
import Repository.CakesShopTextFile;
import Repository.OrdersTextFile;
import Service.OrderService;
import Service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Service.CakeService;
import Domain.Cake;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainController {

    CakesShopTextFile cakesShopTextFile = new CakesShopTextFile("data/cakes.txt");
    OrdersTextFile ordersTextFile = new OrdersTextFile("data/orders.txt", cakesShopTextFile);
    private final CakeService cakeService = new CakeService(cakesShopTextFile);
    private final OrderService orderService = new OrderService(ordersTextFile, cakeService);
    private final ReportService reports = new ReportService(orderService, cakeService);

    @FXML
    public void handleAddCake() {
        try {
            // Collect data for the new cake
            TextInputDialog nameDialog = createInputDialog("Add Cake", "Enter the name of the cake:");
            Optional<String> name = nameDialog.showAndWait();
            if (name.isEmpty()) return;

            TextInputDialog priceDialog = createInputDialog("Add Cake", "Enter the price of the cake:");
            Optional<String> priceStr = priceDialog.showAndWait();
            if (priceStr.isEmpty()) return;

            TextInputDialog descriptionDialog = createInputDialog("Add Cake", "Enter the description of the cake:");
            Optional<String> description = descriptionDialog.showAndWait();
            if (description.isEmpty()) return;

            TextInputDialog sizeDialog = createInputDialog("Add Cake", "Enter the size of the cake:");
            Optional<String> size = sizeDialog.showAndWait();
            if (size.isEmpty()) return;

            TextInputDialog quantityDialog = createInputDialog("Add Cake", "Enter the quantity of the cake:");
            Optional<String> quantityStr = quantityDialog.showAndWait();
            if (quantityStr.isEmpty()) return;

            TextInputDialog idDialog = createInputDialog("Add Cake", "Enter the id of the cake:");
            Optional<String> idStr = idDialog.showAndWait();
            if (idStr.isEmpty()) return;

            Cake cake1 = new Cake(name.get(), Integer.parseInt(priceStr.get()), description.get(), size.get(), Integer.parseInt(quantityStr.get()), Integer.parseInt(idStr.get()));

            // Add the cake to the service
            cakeService.add(cake1.getId(),cake1);
            showInfo("Cake added successfully!");
        } catch (Exception e) {
            showError("Failed to add cake: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateCake() {
        try {
            // Collect the ID of the cake to update
            TextInputDialog idDialog = createInputDialog("Update Cake", "Enter the ID of the cake to update:");
            Optional<String> idStr = idDialog.showAndWait();
            if (idStr.isEmpty()) return;

            int id = Integer.parseInt(idStr.get());

            // Collect updated data for the cake
            TextInputDialog nameDialog = createInputDialog("Update Cake", "Enter the new name of the cake:");
            Optional<String> name = nameDialog.showAndWait();
            if (name.isEmpty()) return;

            TextInputDialog priceDialog = createInputDialog("Update Cake", "Enter the new price of the cake:");
            Optional<String> priceStr = priceDialog.showAndWait();
            if (priceStr.isEmpty()) return;

            TextInputDialog descriptionDialog = createInputDialog("Update Cake", "Enter the new description of the cake:");
            Optional<String> description = descriptionDialog.showAndWait();
            if (description.isEmpty()) return;

            TextInputDialog sizeDialog = createInputDialog("Update Cake", "Enter the new size of the cake:");
            Optional<String> size = sizeDialog.showAndWait();
            if (size.isEmpty()) return;

            TextInputDialog quantityDialog = createInputDialog("Update Cake", "Enter the new quantity of the cake:");
            Optional<String> quantityStr = quantityDialog.showAndWait();
            if (quantityStr.isEmpty()) return;

            // Update the cake in the service
            Cake updatedCake = new Cake(name.get(), Integer.parseInt(priceStr.get()), description.get(), size.get(), Integer.parseInt(quantityStr.get()),id);
            cakeService.update(id, updatedCake);
            showInfo("Cake updated successfully!");
        } catch (Exception e) {
            showError("Failed to update cake: " + e.getMessage());
        }
    }

    @FXML
    public void handleRemoveCake() {
        try {
            // Collect the ID of the cake to remove
            TextInputDialog idDialog = createInputDialog("Remove Cake", "Enter the ID of the cake to remove:");
            Optional<String> idStr = idDialog.showAndWait();
            if (idStr.isEmpty()) return;

            int id = Integer.parseInt(idStr.get());
            Optional<Cake> cake = cakeService.showCake(id);
            Cake cake1 = cake.orElse(null);

            // Remove the cake from the service
            cakeService.remove(id);

            for(Order o: orderService.getOrders())
            {
                if(o.getCake().equals(cake1))
                    orderService.remove(o.getId());
            }
            showInfo("Cake removed successfully!");
        } catch (Exception e) {
            showError("Failed to remove cake: " + e.getMessage());
        }
    }

    @FXML
    public void handleShowCakes() {
        try {
            List<Cake> cakes = cakeService.getCakeList();
            StringBuilder cakeList = new StringBuilder("Cakes:\n");
            for (Cake cake : cakes) {
                cakeList.append(cake).append("\n");
            }
            showInfo(cakeList.toString());
        } catch (Exception e) {
            showError("Failed to retrieve cakes: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddOrder() {
        try {
            TextInputDialog orderIdDialog = createInputDialog("Add Order", "Enter the order ID:");
            Optional<String> orderIdStr = orderIdDialog.showAndWait();
            if (orderIdStr.isEmpty()) return;

            int orderId = Integer.parseInt(orderIdStr.get());

            TextInputDialog cakeIdDialog = createInputDialog("Add Order", "Enter the cake ID for the order:");
            Optional<String> cakeIdStr = cakeIdDialog.showAndWait();
            if (cakeIdStr.isEmpty()) return;

            int cakeId = Integer.parseInt(cakeIdStr.get());


            Optional<Cake> cake = cakeService.showCake(cakeId);
            Order order = new Order(orderId, cake.orElse(null));
            orderService.add(orderId, order);
            showInfo("Order added successfully!");
        } catch (Exception e) {
            showError("Failed to add order: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancelOrder() {
        try {
            TextInputDialog orderIdDialog = createInputDialog("Cancel Order", "Enter the order ID to cancel:");
            Optional<String> orderIdStr = orderIdDialog.showAndWait();
            if (orderIdStr.isEmpty()) return;

            int orderId = Integer.parseInt(orderIdStr.get());

            orderService.remove(orderId);
            showInfo("Order canceled successfully!");
        } catch (Exception e) {
            showError("Failed to cancel order: " + e.getMessage());
        }
    }

    @FXML
    public void handleFinishOrder() {
        try {
            TextInputDialog orderIdDialog = createInputDialog("Finish Order", "Enter the order ID to finish:");
            Optional<String> orderIdStr = orderIdDialog.showAndWait();
            if (orderIdStr.isEmpty()) return;

            int orderId = Integer.parseInt(orderIdStr.get());

            Optional<Order> orderToFinish = orderService.showOrder(orderId);

            Cake cakeToFinish = orderToFinish.get().getCake();
            if (cakeToFinish.getQuantity() > 0) {
                // Reduce cake quantity
                cakeToFinish.setQuantity(cakeToFinish.getQuantity() - 1);
                // Remove the order after reducing the cake quantity
                orderService.remove(orderId);

            }
            showInfo("Order finished successfully!");
        } catch (Exception e) {
            showError("Failed to finish order: " + e.getMessage());
        }
    }

    @FXML
    public void handleVerifyOrder() {
        try {
            TextInputDialog orderIdDialog = createInputDialog("Verify Order", "Enter the order ID to verify:");
            Optional<String> orderIdStr = orderIdDialog.showAndWait();
            if (orderIdStr.isEmpty()) return;

            int orderId = Integer.parseInt(orderIdStr.get());

            Optional<Order> order = orderService.showOrder(orderId);
            showInfo(order.toString());
        } catch (Exception e) {
            showError("Failed to verify order: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateOrder() {
        try {
            TextInputDialog orderIdDialog = createInputDialog("Update Order", "Enter the order ID to update:");
            Optional<String> orderIdStr = orderIdDialog.showAndWait();
            if (orderIdStr.isEmpty()) return;

            int orderId = Integer.parseInt(orderIdStr.get());

            TextInputDialog cakeIdDialog = createInputDialog("Update Order", "Enter the new cake ID for the order:");
            Optional<String> cakeIdStr = cakeIdDialog.showAndWait();
            if (cakeIdStr.isEmpty()) return;

            int cakeId = Integer.parseInt(cakeIdStr.get());


            Optional<Cake> cake = cakeService.showCake(cakeId);
            Order order = new Order(orderId, cake.orElse(null));

            orderService.update(orderId, order);
            showInfo("Order updated successfully!");
        } catch (Exception e) {
            showError("Failed to update order: " + e.getMessage());
        }
    }

    @FXML
    public void handleShowOrders() {
        try {
            List<Order> orders = orderService.getOrders();
            StringBuilder orderList = new StringBuilder("Orders:\n");
            for (Order order : orders) {
                orderList.append(order).append("\n");
            }
            showInfo(orderList.toString());
        } catch (Exception e) {
            showError("Failed to retrieve orders: " + e.getMessage());
        }
    }

    @FXML
    public void handleFilterCakesByPrice() {
        try {
            TextInputDialog priceDialog = createInputDialog("Filter Cakes", "Enter your budget for cakes:");
            Optional<String> priceStr = priceDialog.showAndWait();
            if (priceStr.isEmpty()) return;

            int price = Integer.parseInt(priceStr.get());
            List<Cake> filteredCakes = cakeService.getCakeListByPrice(price);

            if (filteredCakes.isEmpty()) {
                showInfo("No cakes within your budget.");
            } else {
                StringBuilder result = new StringBuilder("Cakes within your budget:\n");
                for (Cake cake : filteredCakes) {
                    result.append(cake).append("\n");
                }
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to filter cakes by price: " + e.getMessage());
        }
    }

    @FXML
    public void handleFilterCakesBySize() {
        try {
            TextInputDialog sizeDialog = createInputDialog("Filter Cakes", "Enter the size of the cake:");
            Optional<String> size = sizeDialog.showAndWait();
            if (size.isEmpty()) return;

            List<Cake> filteredCakes = cakeService.getCakeListBySize(size.get());

            if (filteredCakes.isEmpty()) {
                showInfo("No cakes of this size available.");
            } else {
                StringBuilder result = new StringBuilder("Cakes of the specified size:\n");
                for (Cake cake : filteredCakes) {
                    result.append(cake).append("\n");
                }
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to filter cakes by size: " + e.getMessage());
        }
    }

    @FXML
    public void handleFilterOrdersByCakeName() {
        try {
            TextInputDialog nameDialog = createInputDialog("Filter Orders", "Enter the name of the cake:");
            Optional<String> name = nameDialog.showAndWait();
            if (name.isEmpty()) return;

            List<Order> filteredOrders = orderService.getOrdersByName(name.get());

            if (filteredOrders.isEmpty()) {
                showInfo("No orders for this cake.");
            } else {
                StringBuilder result = new StringBuilder("Orders for the specified cake:\n");
                for (Order order : filteredOrders) {
                    result.append(order).append("\n");
                }
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to filter orders by cake name: " + e.getMessage());
        }
    }

    @FXML
    public void handleFilterOrdersByQuantity() {
        try {
            TextInputDialog quantityDialog = createInputDialog("Filter Orders", "Enter the quantity for filtering orders:");
            Optional<String> quantityStr = quantityDialog.showAndWait();
            if (quantityStr.isEmpty()) return;

            int quantity = Integer.parseInt(quantityStr.get());
            List<Order> filteredOrders = orderService.getOrdersByQuantity(quantity);

            if (filteredOrders.isEmpty()) {
                showInfo("No orders for cakes with this quantity left in stock.");
            } else {
                StringBuilder result = new StringBuilder("Orders for cakes with sufficient stock:\n");
                for (Order order : filteredOrders) {
                    result.append(order).append("\n");
                }
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to filter orders by quantity: " + e.getMessage());
        }
    }

    @FXML
    public void handleReportOrdersForCake() {
        try {
            TextInputDialog cakeNameDialog = createInputDialog("Report: Orders for a Specific Cake", "Enter the cake name:");
            Optional<String> cakeName = cakeNameDialog.showAndWait();
            if (cakeName.isEmpty()) return;

            List<Order> orders = reports.getOrdersForCake(cakeName.get());
            if (orders.isEmpty()) {
                showInfo("No orders found for the cake: " + cakeName.get());
            } else {
                StringBuilder result = new StringBuilder("Orders for cake '" + cakeName.get() + "':\n");
                orders.forEach(order -> result.append(order).append("\n"));
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to generate report: " + e.getMessage());
        }
    }

    @FXML
    public void handleReportRevenuePerSize() {
        try {
            Map<String, Integer> revenuePerSize = reports.getRevenuePerSize();
            if (revenuePerSize.isEmpty()) {
                showInfo("No revenue data available.");
            } else {
                StringBuilder result = new StringBuilder("Revenue per cake size:\n");
                revenuePerSize.forEach((size, revenue) ->
                        result.append("Size: ").append(size).append(", Revenue: ").append(revenue).append("\n"));
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to generate report: " + e.getMessage());
        }
    }

    @FXML
    public void handleReportLowInventoryCakes() {
        try {
            TextInputDialog thresholdDialog = createInputDialog("Report: Low Inventory Cakes", "Enter inventory threshold:");
            Optional<String> thresholdStr = thresholdDialog.showAndWait();
            if (thresholdStr.isEmpty()) return;

            int threshold = Integer.parseInt(thresholdStr.get());
            List<Cake> lowInventoryCakes = reports.getLowInventoryCakes(threshold);

            if (lowInventoryCakes.isEmpty()) {
                showInfo("All cakes have sufficient inventory.");
            } else {
                StringBuilder result = new StringBuilder("Cakes with inventory less than " + threshold + ":\n");
                lowInventoryCakes.forEach(cake -> result.append(cake).append("\n"));
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to generate report: " + e.getMessage());
        }
    }

    @FXML
    public void handleReportMostExpensiveCakePerSize() {
        try {
            Map<String, Cake> expensiveCakes = reports.getMostExpensiveCakePerSize();
            if (expensiveCakes.isEmpty()) {
                showInfo("No data available for expensive cakes.");
            } else {
                StringBuilder result = new StringBuilder("Most expensive cakes by size:\n");
                expensiveCakes.forEach((size, cake) -> {
                    result.append("Size: ").append(size).append("\n  ").append(cake).append("\n");
                });
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to generate report: " + e.getMessage());
        }
    }

    @FXML
    public void handleReportOrderCountByDescription() {
        try {
            Map<String, Long> orderCountByDescription = reports.getOrderCountByDescription();
            if (orderCountByDescription.isEmpty()) {
                showInfo("No orders available for analysis.");
            } else {
                StringBuilder result = new StringBuilder("Order counts by cake description:\n");
                orderCountByDescription.forEach((description, count) ->
                        result.append("Description: ").append(description).append(", Orders: ").append(count).append("\n"));
                showInfo(result.toString());
            }
        } catch (Exception e) {
            showError("Failed to generate report: " + e.getMessage());
        }
    }


    private TextInputDialog createInputDialog(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);
        return dialog;
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
