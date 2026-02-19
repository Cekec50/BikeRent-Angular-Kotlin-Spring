package com.bikerental.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bike")
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Double price;
    
    // Status: 1 = Available, 0 = Rented, -1 = Unavailable
    private Integer status;
    
    private String location;
    
    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "nearest_parking_id")
    private Parking nearestParking;

    public Bike() {
    }

    public Bike(String type, Double price, Integer status, String location, Double latitude, Double longitude, Parking nearestParking) {
        this.type = type;
        this.price = price;
        this.status = status;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nearestParking = nearestParking;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        if (status != null && (status == -1 || status == 0 || status == 1)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Status must be -1, 0, or 1");
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Parking getNearestParking() {
        return nearestParking;
    }

    public void setNearestParking(Parking nearestParking) {
        this.nearestParking = nearestParking;
    }
}
