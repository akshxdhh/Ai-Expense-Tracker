package com.expensetracker.model;

public enum Category {
    FOOD("Food & Dining"),
    TRANSPORT("Transportation"),
    SHOPPING("Shopping"),
    UTILITIES("Utilities"),
    ENTERTAINMENT("Entertainment"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}