package com.example.homeaffairsmobiapp;

import java.io.Serializable;

public class Booking implements Serializable {
    private String bookingId;
    private String userId;
    private String service;
    private String timeSlot;
    private String userName;
    private String userEmail;
    private String status;
    private String userFirstName;
    private String userLastName;


    public Booking() {}

    public Booking(String bookingId, String userId, String service, String timeSlot, String status, String userFirstName, String userLastName, String userEmail) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.service = service;
        this.timeSlot = timeSlot;
        this.userName = userName;
        this.userEmail = userEmail;
        this.status = status; // Add this line
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
    }

    // Getters and setters for all fields
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }
    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }

    public String getFullName() {
        return userFirstName + " " + userLastName;
    }
}
