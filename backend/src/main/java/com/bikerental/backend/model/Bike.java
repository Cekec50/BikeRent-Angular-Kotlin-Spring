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

    public Bike() {
    }

    public Bike(String type, Double price, Integer status, String location) {
        this.type = type;
        this.price = price;
        this.status = status;
        this.location = location;
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
}
