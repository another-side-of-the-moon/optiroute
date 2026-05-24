package ru.optiroute.demo.place;

import jakarta.persistence.*;
import ru.optiroute.demo.place.category.Category;
import ru.optiroute.demo.place.review.Review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Column(name = "city")
    private String city;

    public Place() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (avgTimeSpent == null) {
            avgTimeSpent = getDefaultTimeByCategory();
        }
    }

    private static final Map<String, Double> CATEGORY_TIME_MAP = Map.ofEntries(
            Map.entry("amusement", 1.5),
            Map.entry("art-centers", 2.0),
            Map.entry("art-space", 2.0),
            Map.entry("attractions", 1.5),
            Map.entry("church", 0.75),
            Map.entry("culture", 2.0),
            Map.entry("homesteads", 2.5),
            Map.entry("kids", 2.5),
            Map.entry("monastery", 0.75),
            Map.entry("museums", 4.0),
            Map.entry("palace", 2.5),
            Map.entry("park", 2.0),
            Map.entry("photo-places", 1.0),
            Map.entry("prirodnyj-zapovednik", 3.0),
            Map.entry("recreation", 4.0),
            Map.entry("restaurants", 1.5),
            Map.entry("sights", 1.5),
            Map.entry("suburb", 4.0),
            Map.entry("synagogue", 0.75),
            Map.entry("temple", 0.75),
            Map.entry("theatre", 2.5)
    );

    private Double getDefaultTimeByCategory() {
        if (category == null || category.getKudagoSlug() == null) {
            return 1.5;
        }

        return CATEGORY_TIME_MAP.getOrDefault(
                category.getKudagoSlug().toLowerCase(),
                1.5
        );
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}