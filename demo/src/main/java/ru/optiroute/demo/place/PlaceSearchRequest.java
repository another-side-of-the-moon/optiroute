package ru.optiroute.demo.place;

import java.util.List;

public class PlaceSearchRequest {
    private String query;
    private List<String> categories;
    private Double minRating;
    private Double maxTimeSpent;
    private Double minTimeSpent;
    private Double maxDistance;
    private Double userLatitude;
    private Double userLongitude;
    private Integer page = 0;
    private Integer size = 20;
    private String city;

    public PlaceSearchRequest() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Double getMaxTimeSpent() {
        return maxTimeSpent;
    }

    public void setMaxTimeSpent(Double maxTimeSpent) {
        this.maxTimeSpent = maxTimeSpent;
    }

    public Double getMinTimeSpent() {
        return minTimeSpent;
    }

    public void setMinTimeSpent(Double minTimeSpent) {
        this.minTimeSpent = minTimeSpent;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public Double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(Double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
