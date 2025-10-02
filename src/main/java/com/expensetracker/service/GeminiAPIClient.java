package com.expensetracker.service;

import com.google.gson.*;
import okhttp3.*;
import java.io.*;
import java.util.Base64;

public class GeminiAPIClient {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-vision:generateContent?key=";
    private final String apiKey;
    private final OkHttpClient client;
    private final Gson gson;

    public GeminiAPIClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public String analyzeReceipt(File imageFile) throws IOException {
        String base64Image = encodeImageToBase64(imageFile);

        JsonObject requestBody = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();


        JsonObject textPart = new JsonObject();
        textPart.addProperty("text",
                "Extract the following from this receipt in JSON format: " +
                        "vendor_name, total_amount, date (YYYY-MM-DD), " +
                        "suggested_category (Food/Transport/Shopping/etc), items_list");
        parts.add(textPart);


        JsonObject imagePart = new JsonObject();
        JsonObject inlineData = new JsonObject();
        inlineData.addProperty("mime_type", "image/jpeg");
        inlineData.addProperty("data", base64Image);
        imagePart.add("inline_data", inlineData);
        parts.add(imagePart);

        content.add("parts", parts);
        contents.add(content);
        requestBody.add("contents", contents);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL + "?key=" + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API call failed: " + response);
            }
            return response.body().string();
        }
    }

    private String encodeImageToBase64(File imageFile) throws IOException {
        byte[] fileContent = new FileInputStream(imageFile).readAllBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }
}