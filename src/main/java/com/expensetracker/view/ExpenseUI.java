package com.expensetracker.view;

import com.expensetracker.controller.ExpenseController;
import com.expensetracker.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;

public class ExpenseUI extends Application {
    private ExpenseController controller;
    private ObservableList<Expense> expenseList;
    private TableView<Expense> expenseTable;
    private Label totalLabel;
    private VBox categoryStatsBox;

    @Override
    public void start(Stage primaryStage) {
        controller = new ExpenseController();
        expenseList = FXCollections.observableArrayList(controller.getAllExpenses());

        primaryStage.setTitle("AI-Powered Expense Tracker");

        BorderPane root = new BorderPane();
        root.setTop(createMenuBar());
        root.setCenter(createMainContent());
        root.setRight(createSidePanel());
        root.setBottom(createStatusBar());

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem uploadReceipt = new MenuItem("Upload Receipt");
        MenuItem manualEntry = new MenuItem("Manual Entry");
        MenuItem export = new MenuItem("Export Data");
        MenuItem exit = new MenuItem("Exit");

        uploadReceipt.setOnAction(e -> handleUploadReceipt());
        manualEntry.setOnAction(e -> handleManualEntry());
        export.setOnAction(e -> handleExport());
        exit.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(uploadReceipt, manualEntry,
                new SeparatorMenuItem(), export, new SeparatorMenuItem(), exit);

        Menu viewMenu = new Menu("View");
        MenuItem refresh = new MenuItem("Refresh");
        MenuItem settings = new MenuItem("Settings");

        refresh.setOnAction(e -> refreshData());

        viewMenu.getItems().addAll(refresh, settings);

        menuBar.getMenus().addAll(fileMenu, viewMenu);

        return menuBar;
    }

    private VBox createMainContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Title
        Label title = new Label("Expense Tracker Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Upload button
        Button uploadBtn = new Button("📷 Upload Receipt");
        uploadBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        uploadBtn.setOnAction(e -> handleUploadReceipt());

        // Expense table
        expenseTable = createExpenseTable();

        // Total display
        totalLabel = new Label("Total: $0.00");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        content.getChildren().addAll(title, uploadBtn, expenseTable, totalLabel);
        VBox.setVgrow(expenseTable, Priority.ALWAYS);

        updateTotalLabel();

        return content;
    }

    private TableView<Expense> createExpenseTable() {
        TableView<Expense> table = new TableView<>();
        table.setItems(expenseList);

        TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(100);

        TableColumn<Expense, String> vendorCol = new TableColumn<>("Vendor");
        vendorCol.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        vendorCol.setPrefWidth(200);

        TableColumn<Expense, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(100);
        amountCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        TableColumn<Expense, Category> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);

        TableColumn<Expense, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(100);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(e -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    handleEditExpense(expense);
                });

                deleteBtn.setOnAction(e -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    handleDeleteExpense(expense);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });

        table.getColumns().addAll(dateCol, vendorCol, amountCol, categoryCol, actionCol);

        return table;
    }

    private VBox createSidePanel() {
        VBox sidePanel = new VBox(10);
        sidePanel.setPadding(new Insets(10));
        sidePanel.setPrefWidth(300);
        sidePanel.setStyle("-fx-background-color: #f0f0f0;");

        Label statsTitle = new Label("Category Statistics");
        statsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        categoryStatsBox = new VBox(5);
        updateCategoryStats();

        sidePanel.getChildren().addAll(statsTitle, new Separator(), categoryStatsBox);

        return sidePanel;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #e0e0e0;");

        Label statusLabel = new Label("Ready");
        Label expenseCount = new Label("Expenses: " + expenseList.size());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(statusLabel, spacer, expenseCount);

        return statusBar;
    }

    private void handleUploadReceipt() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Receipt Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            showProgressDialog("Processing receipt...");

            // Process in background thread
            new Thread(() -> {
                Expense expense = controller.processReceipt(selectedFile);

                // Update UI on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    hideProgressDialog();

                    if (expense != null) {
                        showExpenseDialog(expense, true);
                    } else {
                        showAlert("Error", "Failed to process receipt");
                    }
                });
            }).start();
        }
    }

    private void handleManualEntry() {
        Expense newExpense = new Expense();
        newExpense.setDate(LocalDate.now());
        newExpense.setCategory(Category.OTHER);
        showExpenseDialog(newExpense, true);
    }

    private void showExpenseDialog(Expense expense, boolean isNew) {
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle(isNew ? "Add Expense" : "Edit Expense");
        dialog.setHeaderText(isNew ? "Enter expense details" : "Modify expense details");

        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField vendorField = new TextField(expense.getVendor());
        TextField amountField = new TextField(String.valueOf(expense.getAmount()));
        DatePicker datePicker = new DatePicker(expense.getDate());
        ComboBox<Category> categoryBox = new ComboBox<>(
                FXCollections.observableArrayList(Category.values())
        );
        categoryBox.setValue(expense.getCategory());
        TextArea descriptionArea = new TextArea(expense.getDescription());
        descriptionArea.setPrefRowCount(3);

        grid.add(new Label("Vendor:"), 0, 0);
        grid.add(vendorField, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryBox, 1, 3);
        grid.add(new Label("Description:"), 0, 4);
        grid.add(descriptionArea, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                expense.setVendor(vendorField.getText());
                expense.setAmount(Double.parseDouble(amountField.getText()));
                expense.setDate(datePicker.getValue());
                expense.setCategory(categoryBox.getValue());
                expense.setDescription(descriptionArea.getText());
                return expense;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (isNew) {
                controller.addExpense(result);
                expenseList.add(result);
            } else {
                controller.updateExpense(result);
                expenseTable.refresh();
            }
            updateTotalLabel();
            updateCategoryStats();
        });
    }

    private void handleEditExpense(Expense expense) {
        showExpenseDialog(expense, false);
    }

    private void handleDeleteExpense(Expense expense) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Expense");
        alert.setContentText("Are you sure you want to delete this expense?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controller.deleteExpense(expense);
                expenseList.remove(expense);
                updateTotalLabel();
                updateCategoryStats();
            }
        });
    }

    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Expenses");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        fileChooser.setInitialFileName("expenses_" + LocalDate.now() + ".csv");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            controller.exportToCSV(file);
            showAlert("Success", "Expenses exported successfully!");
        }
    }

    private void updateTotalLabel() {
        double total = expenseList.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void updateCategoryStats() {
        categoryStatsBox.getChildren().clear();

        Map<Category, Double> categoryTotals = controller.getCategoryTotals();
        Map<Category, Double> budgetStatus = controller.getBudgetStatus();

        for (Category category : Category.values()) {
            double spent = categoryTotals.getOrDefault(category, 0.0);
            double remaining = budgetStatus.getOrDefault(category, 0.0);

            VBox categoryBox = new VBox(5);
            categoryBox.setPadding(new Insets(5));
            categoryBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");

            Label nameLabel = new Label(category.getDisplayName());
            nameLabel.setStyle("-fx-font-weight: bold;");

            Label spentLabel = new Label(String.format("Spent: $%.2f", spent));

            ProgressBar progressBar = new ProgressBar();
            if (remaining >= 0) {
                double budget = spent + remaining;
                progressBar.setProgress(spent / budget);
                progressBar.setStyle("-fx-accent: green;");
            } else {
                progressBar.setProgress(1.0);
                progressBar.setStyle("-fx-accent: red;");
            }
            progressBar.setPrefWidth(250);

            Label remainingLabel = new Label(
                    String.format("Remaining: $%.2f", Math.max(0, remaining))
            );
            remainingLabel.setTextFill(remaining >= 0 ? Color.GREEN : Color.RED);

            categoryBox.getChildren().addAll(nameLabel, spentLabel, progressBar, remainingLabel);
            categoryStatsBox.getChildren().add(categoryBox);
        }
    }

    private void refreshData() {
        expenseList.clear();
        expenseList.addAll(controller.getAllExpenses());
        updateTotalLabel();
        updateCategoryStats();
    }

    private Dialog<Void> progressDialog;

    private void showProgressDialog(String message) {
        progressDialog = new Dialog<>();
        progressDialog.setTitle("Processing");
        progressDialog.setHeaderText(message);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressDialog.getDialogPane().setContent(progressIndicator);

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.close();
            progressDialog = null;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}