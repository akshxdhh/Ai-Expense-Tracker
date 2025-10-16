package com.expensetracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * The main application class for the AI Expense Tracker.
 * This class builds and displays the main user interface using Java Swing.
 */
public class ExpenseTrackerApp {

    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private JTextField nameField;
    private JTextField amountField;
    private JTextField categoryField;
    private JFrame frame;

    public ExpenseTrackerApp() {
        // Initialize the database
        DatabaseHelper.initializeDatabase();
        // Create the main application frame
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Set a modern Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("AI Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the frame

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(mainPanel);

        // --- Input Panel ---
        mainPanel.add(createInputPanel(), BorderLayout.NORTH);

        // --- Expense Table ---
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        // Load initial data into the table
        loadExpenses();

        // Display the window.
        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and TextFields
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; nameField = new JTextField(15); inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; amountField = new JTextField(15); inputPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; categoryField = new JTextField(15); inputPanel.add(categoryField, gbc);

        // Buttons
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 3; gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(createButtonPanel(), gbc);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        JButton addButton = new JButton("Add Expense");

        addButton.addActionListener(e -> addExpenseAction());

        buttonPanel.add(addButton);
        return buttonPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "Name", "Amount", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setFillsViewportHeight(true);
        return new JScrollPane(expenseTable);
    }

    private void addExpenseAction() {
        String name = nameField.getText();
        String amountStr = amountField.getText();
        String category = categoryField.getText();

        if (name.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            Expense expense = new Expense(name, amount, category);
            DatabaseHelper.addExpense(expense);
            loadExpenses(); // Refresh table
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for the amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExpenses() {
        tableModel.setRowCount(0); // Clear existing data
        List<Expense> expenses = DatabaseHelper.getAllExpenses();
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{expense.getId(), expense.getName(), expense.getAmount(), expense.getCategory()});
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        amountField.setText("");
        categoryField.setText("");
    }

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(ExpenseTrackerApp::new);
    }
}

