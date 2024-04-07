package com.example.budgetapp.model;

import java.sql.Date;

public class Transaction {
    private Long transactionId;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private Double amount;
    private java.sql.Date transactionDate;
    private String description;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Constructors
    public Transaction() {}

    public Transaction(Long transactionId, Long userId, Long categoryId, Double amount, Date transactionDate, String description) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    public Transaction(Long transactionId, Long userId, String categoryName, Double amount, java.sql.Date transactionDate, String description) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    public Transaction(Long userId, Long categoryId, Double amount, java.sql.Date transactionDate, String description) {
        this.userId = userId;
        this.categoryId = categoryId; // This line assumes you have a categoryId field
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

