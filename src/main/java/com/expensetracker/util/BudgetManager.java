package com.expensetracker.util;

import com.expensetracker.model.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetManager {
    private final List<Expense> expenses;
    private final Map<Category, Double> categoryBudgets;

    public BudgetManager() {
        this.expenses = new ArrayList<>();
        this.categoryBudgets = new HashMap<>();
        initializeDefaultBudgets();
    }

    private void initializeDefaultBudgets() {
        categoryBudgets.put(Category.FOOD, 500.0);
        categoryBudgets.put(Category.TRANSPORT, 200.0);
        categoryBudgets.put(Category.SHOPPING, 300.0);
        categoryBudgets.put(Category.UTILITIES, 150.0);
        categoryBudgets.put(Category.ENTERTAINMENT, 200.0);
        categoryBudgets.put(Category.HEALTHCARE, 100.0);
        categoryBudgets.put(Category.EDUCATION, 100.0);
        categoryBudgets.put(Category.OTHER, 100.0);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public double getTotalExpenses() {
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public Map<Category, Double> getCategoryTotals() {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    public Map<Category, Double> getBudgetStatus() {
        Map<Category, Double> status = new HashMap<>();
        Map<Category, Double> totals = getCategoryTotals();

        for (Category category : Category.values()) {
            double budget = categoryBudgets.getOrDefault(category, 0.0);
            double spent = totals.getOrDefault(category, 0.0);
            double remaining = budget - spent;
            status.put(category, remaining);
        }

        return status;
    }

    public List<Expense> getExpensesByMonth(int year, int month) {
        return expenses.stream()
                .filter(e -> e.getDate().getYear() == year &&
                        e.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }
}