package com.tarasiuk.project.hotel.model;

public enum UserRole {
    CLIENT("CLIENT"),
    ADMIN("ADMIN");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
