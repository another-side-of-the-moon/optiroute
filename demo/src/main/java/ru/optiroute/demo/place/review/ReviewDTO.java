package ru.optiroute.demo.place.review;

public class ReviewDTO {
    private Long id;
    private String userName;
    private String text;
    private Integer rating;
    private String createdAt;

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, String userName, String text, Integer rating, String createdAt) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}