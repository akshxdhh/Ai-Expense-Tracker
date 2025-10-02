package com.expensetracker.controller;

import com.expensetracker.model.*;
import com.expensetracker.service.ReceiptProcessorService;
import com.expensetracker.util.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseController {
    private final ReceiptProcessorService receiptProcessor;
    private final BudgetManager budgetManager;
    private final DataPersistence dataPersistence;
    private final List<Expense> expenses;

    public ExpenseController() {
        // Load API key from config
        String apiKey = loadApiKey();
        this.receiptProcessor = new ReceiptProcessorService(apiKey);
        this.budgetManager = new BudgetManager();
        this.dataPersistence = new DataPersistence();
        this.expenses = dataPersistence.loadExpenses();

        // Add loaded expenses to budget manager
        expenses.forEach(budgetManager::addExpense);
    }

    private String loadApiKey() {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {
            props.load(input);
            return props.getProperty("gemini.api.key");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Expense processReceipt(File receiptImage) {
        return receiptProcessor.processReceipt(receiptImage);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        budgetManager.addExpense(expense);
        dataPersistence.saveExpenses(expenses);
    }

    public void updateExpense(Expense expense) {
        dataPersistence.saveExpenses(expenses);
    }

    public void deleteExpense(Expense expense) {
        expenses.remove(expense);
        dataPersistence.saveExpenses(expenses);
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    public Map<Category, Double> getCategoryTotals() {
        return budgetManager.getCategoryTotals();
    }

    public Map<Category, Double> getBudgetStatus() {
        return budgetManager.getBudgetStatus();
    }

    public void exportToCSV(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Date,Vendor,Amount,Category,Description");
            for (Expense expense : expenses) {
                writer.printf("%s,%s,%.2f,%s,%s%n",
                        expense.getDate(),
                        expense.getVendor(),
                        expense.getAmount(),
                        expense.getCategory(),
                        expense.getDescription() != null ? expense.getDescription() : ""
                );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}