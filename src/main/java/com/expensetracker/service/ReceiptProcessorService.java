package com.expensetracker.service;

import com.expensetracker.model.*;
import com.google.gson.*;
import java.io.File;
import java.time.LocalDate;

public class ReceiptProcessorService {
    private final GeminiAPIClient geminiClient;
    private final Gson gson;

    public ReceiptProcessorService(String apiKey) {
        this.geminiClient = new GeminiAPIClient(apiKey);
        this.gson = new Gson();
    }

    public Expense processReceipt(File receiptImage) {
        try {
            String apiResponse = geminiClient.analyzeReceipt(receiptImage);
            return parseApiResponse(apiResponse, receiptImage.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            return createManualExpense();
        }
    }

    private Expense parseApiResponse(String jsonResponse, String imagePath) {
        try {
            JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);
            JsonObject extractedData = response
                    .getAsJsonArray("candidates").get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts").get(0).getAsJsonObject();

            String textContent = extractedData.get("text").getAsString();
            // Parse the JSON from text content
            JsonObject data = gson.fromJson(
                    textContent.substring(textContent.indexOf("{"),
                            textContent.lastIndexOf("}") + 1),
                    JsonObject.class
            );

            Expense expense = new Expense();
            expense.setVendor(data.get("vendor_name").getAsString());
            expense.setAmount(data.get("total_amount").getAsDouble());
            expense.setDate(LocalDate.parse(data.get("date").getAsString()));
            expense.setCategory(mapCategory(data.get("suggested_category").getAsString()));
            expense.setReceiptImagePath(imagePath);

            return expense;
        } catch (Exception e) {
            e.printStackTrace();
            return createManualExpense();
        }
    }

    private Category mapCategory(String categoryStr) {
        try {
            return Category.valueOf(categoryStr.toUpperCase());
        } catch (Exception e) {
            return Category.OTHER;
        }
    }

    private Expense createManualExpense() {
        Expense expense = new Expense();
        expense.setVendor("Manual Entry Required");
        expense.setAmount(0.0);
        expense.setDate(LocalDate.now());
        expense.setCategory(Category.OTHER);
        return expense;
    }
}