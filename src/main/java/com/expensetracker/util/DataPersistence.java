package com.expensetracker.util;

import com.expensetracker.model.Expense;
import com.google.gson.*;
import java.io.*;
import java.util.*;

public class DataPersistence {
    private static final String DATA_FILE = "expenses.json";
    private final Gson gson;

    public DataPersistence() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    public void saveExpenses(List<Expense> expenses) {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> loadExpenses() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Expense[] expenses = gson.fromJson(reader, Expense[].class);
            return new ArrayList<>(Arrays.asList(expenses != null ? expenses : new Expense[0]));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}