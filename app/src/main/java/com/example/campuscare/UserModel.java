package com.example.campuscare;

public class UserModel {
    private int userId;
    private String username, role;

    public UserModel(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}