package com.expensetracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * The main class for the Manual Expense Tracker application.
 * This class builds the Swing UI and handles user interactions for manual entry.
 */
public class ExpenseTrackerApp extends JFrame {

    private final DefaultTableModel tableModel;
    private final JTable expenseTable;
    private final JTextField nameField;
    private final JTextField amountField;
    private final JTextField categoryField;
    private final JLabel totalLabel; // Label to display the total expenses

    public ExpenseTrackerApp() {
        // --- Initialize Database ---
        DatabaseHelper.initializeDatabase();

        // --- Frame Setup ---
        setTitle("Manual Expense Tracker");
        setSize(800, 650); // Increased height for the new panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Table Setup ---
        String[] columnNames = {"ID", "Date", "Name", "Amount ($)", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        expenseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Expenses"));

        // --- Input Panel Setup ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));
        nameField = new JTextField();
        amountField = new JTextField();
        categoryField = new JTextField();
        inputPanel.add(new JLabel(" Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel(" Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel(" Category:"));
        inputPanel.add(categoryField);
        JButton addButton = new JButton("Add Expense");
        inputPanel.add(new JLabel()); // Placeholder for layout
        inputPanel.add(addButton);

        // --- Button Panel Setup ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Delete Selected Expense");
        buttonPanel.add(deleteButton);

        // --- Total Expense Panel ---
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        totalLabel = new JLabel("Total Expenses: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        // --- Main Panel for Inputs and Buttons ---
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(totalPanel, BorderLayout.SOUTH);

        // --- Add components to frame ---
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        addButton.addActionListener(e -> addExpenseManually());
        deleteButton.addActionListener(e -> deleteExpense());

        // --- Load initial data into the table ---
        refreshExpenseTable();
    }

    /**
     * Clears the table, reloads all expenses, and updates the total.
     */
    private void refreshExpenseTable() {
        tableModel.setRowCount(0); // Clear existing data
        List<Expense> expenses = DatabaseHelper.getAllExpenses();
        double total = 0.0;
        for (Expense expense : expenses) {
            Object[] row = {
                    expense.getId(),
                    expense.getFormattedDate(),
                    expense.getName(),
                    expense.getAmount(),
                    expense.getCategory()
            };
            tableModel.addRow(row);
            total += expense.getAmount();
        }
        // Update the total label, formatted to two decimal places
        totalLabel.setText(String.format("Total Expenses: $%.2f", total));
    }

    private void addExpenseManually() {
        try {
            String name = nameField.getText().trim();
            String amountStr = amountField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountStr);
            Expense newExpense = new Expense(0, name, amount, category, LocalDate.now());
            DatabaseHelper.addExpense(newExpense);
            refreshExpenseTable();

            // Clear input fields
            nameField.setText("");
            amountField.setText("");
            categoryField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this expense?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DatabaseHelper.deleteExpense(id);
                refreshExpenseTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an expense from the table to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use the system's look and feel for a native appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ExpenseTrackerApp().setVisible(true);
        });
    }
}

