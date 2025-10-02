package com.expensetracker.util;

import com.expensetracker.model.Expense;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {

    private static final String FILE_PATH = "expenses.json";

    private final Gson gson;

    public DataPersistence() {
        this.gson = new GsonBuilder()

                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public List<Expense> loadExpenses() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            // Using TypeToken is a robust way to deserialize a generic list.
            Type type = new TypeToken<ArrayList<Expense>>() {}.getType();
            List<Expense> expenses = gson.fromJson(reader, type);
            // Ensure we never return null, return an empty list instead.
            return expenses != null ? expenses : new ArrayList<>();
        } catch (IOException e) {
            // This is expected if the file doesn't exist yet.
            System.out.println("No existing expense file found. A new one will be created on save.");
            return new ArrayList<>();
        }
    }

    public void saveExpenses(List<Expense> expenses) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            System.err.println("Error saving expenses to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
