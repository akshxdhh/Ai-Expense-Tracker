package com.expensetracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations for the expense tracker application.
 * Uses SQLite for local, file-based storage.
 */
public class DatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:expensetracker.db";

    /**
     * Initializes the database connection and creates the expenses table if it doesn't exist.
     */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL,"
                + " amount REAL NOT NULL,"
                + " category TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Retrieves all expenses from the database.
     * @return A list of Expense objects.
     */
    public static List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT id, name, amount, category FROM expenses";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("amount"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * Adds a new expense record to the database.
     * @param expense The Expense object to add.
     */
    public static void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses(name, amount, category) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, expense.getName());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getCategory());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
        }
    }
}

