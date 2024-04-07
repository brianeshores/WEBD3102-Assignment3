package com.example.budgetapp.model;

public class Category {
    private Long categoryId;
    private String categoryName;
    private String type;

    // Constructors
    public Category() {}

    public Category(long categoryId, String categoryName, String type) {
        this.categoryName = categoryName;
        this.type = type;
        this.categoryId = categoryId;
    }

    // Getters and setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

