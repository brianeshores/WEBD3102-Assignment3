package com.example.budgetapp.model;

public class User {
    private int userId;
    private String userName;
    private String email;
    private String password; // Ensure this is securely hashed

    // Constructors
    public User() {}

    public User(int userId, String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

