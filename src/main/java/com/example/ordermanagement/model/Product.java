package com.example.ordermanagement.model;

public class Product {
    private int productId; // Updated from 'id' to 'productId'
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;

    public Product() {
    }

    public Product(int productId, String name, String description, double price, int quantity, String category) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public Product(String name, String description, double price, int quantity, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // Getters and Setters
    public int getProductId() { // Updated from 'getId' to 'getProductId'
        return productId;
    }

    public void setProductId(int productId) { // Updated from 'setId' to 'setProductId'
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
