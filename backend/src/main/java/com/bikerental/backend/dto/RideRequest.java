package com.bikerental.backend.dto;

import com.bikerental.backend.model.Bike;
import com.bikerental.backend.model.User;

public class RideRequest {
    private String startTime;
    private Bike bike;
    private User user;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
