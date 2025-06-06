package com.mumflood.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flood_report")
public class FloodReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String height;

    @Column(name = "water_level")
    private String waterLevel;

    private String location;

    private String feedback;

    private LocalDateTime timestamp;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }

    public String getWaterLevel() {
        return waterLevel;
    }
    public void setWaterLevel(String waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getFeedback() {
        return feedback;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
