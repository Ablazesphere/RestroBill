package com.example.myapplication;

public class User {
    private String id;
    private String email;
    private String createdDate;
    private String signedInDate;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email, String createdDate, String signedInDate) {
        this.id = id;
        this.email = email;
        this.createdDate = createdDate;
        this.signedInDate = signedInDate;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getSignedInDate() {
        return signedInDate;
    }
}
