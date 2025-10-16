package com.expensetracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single expense record. This class acts as the data model
 * for an expense, holding its properties like ID, name, amount, category, and date.
 */
public class Expense {
    private final int id;
    private final String name;
    private final double amount;
    private final String category;
    private final LocalDate date;

    // A formatter to ensure dates are consistently handled as "yyyy-MM-dd".
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor for creating an Expense object.
     * @param id The unique identifier of the expense (usually from the database).
     * @param name The name or description of the expense (e.g., "Coffee").
     * @param amount The monetary value of the expense.
     * @param category The category of the expense (e.g., "Food").
     * @param date The date the expense occurred.
     */
    public Expense(int id, String name, double amount, String category, LocalDate date) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }

    /**
     * Provides the date as a formatted string for display or database storage.
     * @return The date formatted as "yyyy-MM-dd", or "N/A" if the date is null.
     */
    public String getFormattedDate() {
        if (date != null) {
            return date.format(DATE_FORMATTER);
        }
        return "N/A";
    }
}

