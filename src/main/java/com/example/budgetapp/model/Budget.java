package com.example.budgetapp.model;

public class Budget {
    private long budgetId;
    private long userId;
    private long categoryId;
    private Double amount;
    private String month; // Updated comment to reflect usage as a general date string or specific month representation
    private String categoryName; // New field for category name
    private transient double utilizationPercentage;
    // Default Constructor
    public Budget() {
    }

    // Constructor with all fields including categoryName
    public Budget(long budgetId, long userId, long categoryId, Double amount, String month, String categoryName) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.month = month;
        this.categoryName = categoryName; // Initialize the new field
    }

    public Budget(long budgetId, long userId, long categoryId, Double amount, String month) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.month = month;
    }

    // Getters and Setters

    public double getUtilizationPercentage() {
        return utilizationPercentage;
    }

    public void setUtilizationPercentage(double utilizationPercentage) {
        this.utilizationPercentage = utilizationPercentage;
    }

    public long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(long budgetId) { // Fixed type to match field
        this.budgetId = budgetId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) { // Fixed type to match field
        this.userId = userId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) { // Fixed type to match field
        this.categoryId = categoryId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCategoryName() { // Getter for categoryName
        return categoryName;
    }

    public void setCategoryName(String categoryName) { // Setter for categoryName
        this.categoryName = categoryName;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Budget{" +
                "budgetId=" + budgetId +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", month='" + month + '\'' +
                ", categoryName='" + categoryName + '\'' + // Include categoryName in the toString method
                '}';
    }
}