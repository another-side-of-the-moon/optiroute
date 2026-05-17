package ru.optiroute.demo.place;

import java.util.ArrayList;
import java.util.List;

public class PlaceDTO {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private Double avgRating;
    private Double avgTimeSpent;
    private String avgTimeSpentFormatted;
    private List<ReviewDTO> reviews = new ArrayList<>();
    private String estimatedTime;
    private String timeSpentAtPlace;

    public PlaceDTO() {
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Double getAvgTimeSpent() {
        return avgTimeSpent;
    }

    public void setAvgTimeSpent(Double avgTimeSpent) {
        this.avgTimeSpent = avgTimeSpent;
    }

    public String getAvgTimeSpentFormatted() {
        return avgTimeSpentFormatted;
    }

    public void setAvgTimeSpentFormatted(String avgTimeSpentFormatted) {
        this.avgTimeSpentFormatted = avgTimeSpentFormatted;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getTimeSpentAtPlace() {
        return timeSpentAtPlace;
    }

    public void setTimeSpentAtPlace(String timeSpentAtPlace) {
        this.timeSpentAtPlace = timeSpentAtPlace;
    }
}