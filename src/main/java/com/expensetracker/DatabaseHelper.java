package com.expensetracker;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations (CRUD - Create, Read, Update, Delete)
 * for the expenses using JDBC and a local SQLite database.
 */
public class DatabaseHelper {

    // The connection string for the SQLite database.
    // This will create a file named "expensetracker.db" in the project's root directory.
    private static final String DATABASE_URL = "jdbc:sqlite:expensetracker.db";

    /**
     * Initializes the database by creating the 'expenses' table if it does not already exist.
     * This is a safe operation to run every time the application starts.
     */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL,"
                + " amount REAL NOT NULL,"
                + " category TEXT NOT NULL,"
                + " date TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Adds a new expense record to the database.
     * @param expense The Expense object containing the data to add.
     */
    public static void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses(name, amount, category, date) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, expense.getName());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getCategory());
            pstmt.setString(4, expense.getFormattedDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
        }
    }

    /**
     * Retrieves all expense records from the database.
     * @return A list of all Expense objects.
     */
    public static List<Expense> getAllExpenses() {
        String sql = "SELECT id, name, amount, category, date FROM expenses";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and create Expense objects
            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        LocalDate.parse(rs.getString("date")) // Convert string from DB back to LocalDate
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * Deletes an expense from the database based on its unique ID.
     * @param id The ID of the expense to delete.
     */
    public static void deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the corresponding parameter
            pstmt.setInt(1, id);
            // Execute the delete statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
        }
    }
}

