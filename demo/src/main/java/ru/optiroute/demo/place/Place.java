package ru.optiroute.demo.place;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private String address;

    @Column(name = "avg_rating")
    private Double avgRating = 0.0;

    @Column(name = "kudago_id")
    private Long kudagoId;

    @Column(name = "avg_time_spent")
    private Double avgTimeSpent;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Place() {
    }

    public Place(Long id, String name, Category category, String description,
                 String imageUrl, Double latitude, Double longitude, String address,
                 Double avgRating, Long kudagoId, Double avgTimeSpent) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.avgRating = avgRating;
        this.kudagoId = kudagoId;
        this.avgTimeSpent = avgTimeSpent;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (avgTimeSpent == null) {
            avgTimeSpent = getDefaultTimeByCategory();
        }
    }

    private Double getDefaultTimeByCategory() {
        if (category == null) return 1.0;

        String categoryName = category.getName().toLowerCase();

        if (categoryName.contains("музей") || categoryName.contains("museum")) {
            return 3.0;
        } else if (categoryName.contains("парк") || categoryName.contains("park")) {
            return 1.5;
        } else if (categoryName.contains("ресторан") || categoryName.contains("restaurant")
                || categoryName.contains("кафе") || categoryName.contains("cafe")) {
            return 1.5;
        } else if (categoryName.contains("театр") || categoryName.contains("theatre")) {
            return 2.5;
        } else if (categoryName.contains("кино") || categoryName.contains("cinema")) {
            return 2.0;
        } else if (categoryName.contains("галерея") || categoryName.contains("gallery")) {
            return 2.0;
        } else if (categoryName.contains("зоопарк") || categoryName.contains("zoo")) {
            return 3.0;
        } else if (categoryName.contains("магазин") || categoryName.contains("shop")
                || categoryName.contains("молл") || categoryName.contains("mall")) {
            return 1.5;
        } else if (categoryName.contains("собор") || categoryName.contains("церковь")
                || categoryName.contains("храм") || categoryName.contains("church")) {
            return 1.0;
        } else if (categoryName.contains("выставка") || categoryName.contains("exhibition")) {
            return 1.5;
        }

        return 1.0;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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

    public Long getKudagoId() {
        return kudagoId;
    }

    public void setKudagoId(Long kudagoId) {
        this.kudagoId = kudagoId;
    }

    public Double getAvgTimeSpent() {
        return avgTimeSpent;
    }

    public void setAvgTimeSpent(Double avgTimeSpent) {
        this.avgTimeSpent = avgTimeSpent;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}