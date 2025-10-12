package com.expensetracker;

import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Service class to interact with the Google Gemini API.
 * Handles image processing and API communication to extract expense details from receipts.
 */
public class GeminiApiService {

    // IMPORTANT: Replace with your actual Gemini API Key
    private static final String API_KEY = "AIzaSyBF0pGx41P_Cu5E0FpvBhMf8nfJwk2Sito";

    // --- THIS IS THE CORRECTED LINE ---
    // Reverting to the most stable and widely available model for image and text tasks.
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-vision:generateContent?key=" + API_KEY;
    // ------------------------------------

    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Analyzes a receipt image using the Gemini API and extracts expense details.
     *
     * @param imagePath The path to the receipt image file.
     * @return An Expense object with the extracted details, or null if an error occurs.
     */
    public static Expense analyzeReceipt(Path imagePath) {
        if ("YOUR_API_KEY_HERE".equals(API_KEY)) {
            JOptionPane.showMessageDialog(null, "Please add your Gemini API Key to GeminiApiService.java", "API Key Missing", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            // 1. Read image and encode to Base64
            byte[] imageBytes = Files.readAllBytes(imagePath);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // 2. Create the JSON request payload
            String prompt = "Analyze this receipt. Extract only the store or vendor name, the final total amount, and suggest a single expense category (e.g., 'Groceries', 'Dining', 'Utilities', 'Transport'). Format the output as a JSON object with keys: 'name', 'amount', 'category'. Do not include any other text or formatting.";
            String jsonPayload = buildJsonPayload(base64Image, prompt);

            // 3. Send the request to the Gemini API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("API Error: " + response.body());
                JOptionPane.showMessageDialog(null, "API Error: " + response.body(), "API Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // 4. Parse the JSON response
            return parseApiResponse(response.body());

        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing receipt: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String buildJsonPayload(String base64Image, String prompt) {
        JSONObject inlineData = new JSONObject();
        inlineData.put("mime_type", "image/jpeg");
        inlineData.put("data", base64Image);

        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);

        JSONObject imagePart = new JSONObject();
        imagePart.put("inline_data", inlineData);

        JSONObject content = new JSONObject();
        content.put("parts", new Object[]{textPart, imagePart});

        JSONObject root = new JSONObject();
        root.put("contents", new Object[]{content});

        return root.toString();
    }

    private static Expense parseApiResponse(String responseBody) {
        try {
            JSONObject root = new JSONObject(responseBody);
            // Navigate through the JSON structure to get the text content
            String textContent = root.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            // Clean up the text content which might be wrapped in markdown JSON block
            String cleanedJson = textContent.replace("```json", "").replace("```", "").trim();

            JSONObject expenseJson = new JSONObject(cleanedJson);

            String name = expenseJson.getString("name");
            // Handle cases where amount might be a string with a currency symbol
            String amountStr = expenseJson.get("amount").toString().replaceAll("[^\\d.]", "");
            double amount = Double.parseDouble(amountStr);
            String category = expenseJson.getString("category");

            return new Expense(name, amount, category);
        } catch (Exception e) {
            System.err.println("Error parsing API response: " + e.getMessage());
            System.err.println("Response Body: " + responseBody);
            return null;
        }
    }
}