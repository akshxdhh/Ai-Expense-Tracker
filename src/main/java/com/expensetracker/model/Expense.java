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

    // --- GETTERS AND SETTERS ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptImagePath() {
        return receiptImagePath;
    }

    public void setReceiptImagePath(String receiptImagePath) {
        this.receiptImagePath = receiptImagePath;
    }

    // --- Private Helper Methods ---

    private String generateId() {
        // Generates a simple unique ID based on the current time.
        return "EXP_" + System.currentTimeMillis();
    }


    // --- Overridden Methods ---

    @Override
    public String toString() {
        // A more concise and readable string representation of the expense.
        return String.format("%s - %s: $%.2f (%s)",
                date, vendor, amount, category);
    }
}
