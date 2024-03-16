package com.example.ordermanagement.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int orderId;
    private int userId;
    private String status;
    private Map<Integer, Integer> productCounts;
    private ArrayList<Product> products;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Order() {
        this.productCounts = new HashMap<>();
        this.products = new ArrayList<>(); // Initialize the products list

    }

    public Order(int orderId, int userId, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.productCounts = new HashMap<>();
        this.products = new ArrayList<>();

    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<Integer, Integer> getProductCounts() {
        return productCounts;
    }

    public void setProductCounts(Map<Integer, Integer> productCounts) {
        this.productCounts = productCounts;
    }



    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>(); // Additional safety check
        }
        this.products.add(product);
        this.productCounts.put(product.getProductId(), this.productCounts.getOrDefault(product.getProductId(), 0) + 1);
    }

    public void removeProduct(Product product) {
        if (this.products != null) {
            this.products.remove(product);
            int currentCount = this.productCounts.getOrDefault(product.getProductId(), 0);
            if (currentCount > 1) {
                this.productCounts.put(product.getProductId(), currentCount - 1);
            } else {
                this.productCounts.remove(product.getProductId());
            }
        }
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}
