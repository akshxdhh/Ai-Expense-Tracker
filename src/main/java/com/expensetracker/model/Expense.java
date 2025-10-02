package com.expensetracker.model;

import java.time.LocalDate;

public class Expense {
    private String id;
    private String vendor;
    private double amount;
    private LocalDate date;
    private Category category;
    private String description;
    private String receiptImagePath;

    // Constructors
    public Expense() {
        this.id = generateId();
    }

    // Getters and Setters
    // ... implement all getters/setters

    private String generateId() {
        return "EXP_" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return String.format("%s - %s: $%.2f (%s)",
                date, vendor, amount, category);
    }
}