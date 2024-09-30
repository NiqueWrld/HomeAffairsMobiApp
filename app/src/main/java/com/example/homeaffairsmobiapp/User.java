package com.example.homeaffairsmobiapp;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePictureUrl;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    public User(String firstName, String lastName, String email, String profilePictureUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
