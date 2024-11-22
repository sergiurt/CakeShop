package gui;

import Repository.EntityRepository.MemoryRepository;
import Repository.EntityRepository.Repository;
import Repository.FilesRepository.RepositoryFactory;
import Service.CarService;
import domain.Car;
import domain.Reservation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;
import Settings.Settings;


public class CarView extends Application {
    private CarController controller;
    Settings settings = new Settings("A6/src/Settings/settings.properties");
    MemoryRepository<String, Car> carRepository = RepositoryFactory.createCarRepository(settings);
    MemoryRepository<String, Reservation> reservationRepository = RepositoryFactory.createReservationRepository(settings);
    private CarService carService;

    private TextField idField;
    private TextField makeField;
    private TextField modelField;
    private TextField yearField;
    private TextField rateField;
    private TableView<Car> carTable;
    private Label messageLabel;


    @Override
    public void start(Stage primaryStage) {
        carService = new CarService(carRepository, reservationRepository);
        this.controller = new CarController(carService);

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        // Create form layout
        GridPane form = createForm();

        // Create table
        carTable = createTable();

        // Create buttons
        HBox buttonBox = createButtons();

        // Message label for feedback
        messageLabel = new Label();
        messageLabel.setStyle("-fx-font-weight: bold;");

        mainLayout.getChildren().addAll(
                new Label("Car Management System"),
                form,
                buttonBox,
                messageLabel,
                carTable
        );

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setTitle("Car Management");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial load of data
        refreshTable();
    }

    private GridPane createForm() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(5);
        form.setPadding(new Insets(10));

        // Create fields
        idField = new TextField();
        makeField = new TextField();
        modelField = new TextField();
        yearField = new TextField();
        rateField = new TextField();

        // Add form elements
        form.addRow(0, new Label("ID:"), idField);
        form.addRow(1, new Label("Make:"), makeField);
        form.addRow(2, new Label("Model:"), modelField);
        form.addRow(3, new Label("Year:"), yearField);
        form.addRow(4, new Label("Daily Rate:"), rateField);

        return form;
    }

    private HBox createButtons() {
        HBox buttonBox = new HBox(10);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button clearButton = new Button("Clear");

        addButton.setOnAction(e -> handleAdd());
        updateButton.setOnAction(e -> handleUpdate());
        deleteButton.setOnAction(e -> handleDelete());
        clearButton.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        return buttonBox;
    }

    private TableView<Car> createTable() {
        TableView<Car> table = new TableView<>();

        // Create columns
        TableColumn<Car, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Car, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));

        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Car, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Car, Double> rateCol = new TableColumn<>("Daily Rate");
        rateCol.setCellValueFactory(new PropertyValueFactory<>("dailyRate"));

        table.getColumns().addAll(idCol, makeCol, modelCol, yearCol, rateCol);

        // Add selection listener
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                }
        );

        return table;
    }

    private void handleAdd() {
        try {
            controller.addCar(idField.getText(),
                    makeField.getText(),
                    modelField.getText(),
                    yearField.getText(),
                    rateField.getText());
            showMessage("Car added successfully!", true);
            clearForm();
            refreshTable();
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), false);
        }
    }

    private void handleUpdate() {
        try {
            controller.updateCar(idField.getText(),
                    makeField.getText(),
                    modelField.getText(),
                    yearField.getText(),
                    rateField.getText());
            showMessage("Car updated successfully!", true);
            refreshTable();
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), false);
        }
    }

    private void handleDelete() {
        String id = idField.getText();
        if (id.isEmpty()) {
            showMessage("Please select a car to delete", false);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Car");
        alert.setContentText("Are you sure you want to delete this car?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                controller.deleteCar(id);
                showMessage("Car deleted successfully!", true);
                clearForm();
                refreshTable();
            } catch (Exception e) {
                showMessage("Error: " + e.getMessage(), false);
            }
        }
    }

    private void populateForm(Car car) {
        idField.setText(car.getId());
        makeField.setText(car.getMake());
        modelField.setText(car.getModel());
        yearField.setText(String.valueOf(car.getYear()));
        rateField.setText(String.valueOf(car.getDailyRate()));
    }

    private void clearForm() {
        idField.clear();
        makeField.clear();
        modelField.clear();
        yearField.clear();
        rateField.clear();
        messageLabel.setText("");
    }

    private void refreshTable() {
        carTable.getItems().clear();
        carTable.getItems().addAll(controller.getAllCars());
    }

    private void showMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        messageLabel.setStyle(isSuccess ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
