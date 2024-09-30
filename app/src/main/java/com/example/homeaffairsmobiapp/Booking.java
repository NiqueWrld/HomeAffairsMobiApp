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

    public Booking() {}

    public Booking(String bookingId, String userId, String service, String timeSlot, String userName, String userEmail) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.service = service;
        this.timeSlot = timeSlot;
        this.userName = userName;
        this.userEmail = userEmail;
        this.status = status; // Add this line
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
}
