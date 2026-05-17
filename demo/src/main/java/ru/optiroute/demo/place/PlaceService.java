package ru.optiroute.demo.place;
import ru.optiroute.demo.place.PlaceDTO;
import ru.optiroute.demo.place.PlaceSearchRequest;
import ru.optiroute.demo.place.ReviewDTO;
import ru.optiroute.demo.place.Place;
import ru.optiroute.demo.place.Review;
import ru.optiroute.demo.place.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final KudaGoService kudaGoService;

    public PlaceService(PlaceRepository placeRepository, KudaGoService kudaGoService) {
        this.placeRepository = placeRepository;
        this.kudaGoService = kudaGoService;
    }

    public List<PlaceDTO> searchPlaces(PlaceSearchRequest request) {
        List<Place> places;

        String query = request.getQuery();
        String category = request.getCategories() != null && !request.getCategories().isEmpty()
                ? request.getCategories().get(0) : null;
        Double minRating = request.getMinRating();

        if (query != null && !query.isEmpty() && category != null && minRating != null) {
            places = placeRepository.findByNameContainingIgnoreCase(query).stream()
                    .filter(p -> p.getCategory() != null && category.equals(p.getCategory().getName()))
                    .filter(p -> p.getAvgRating() >= minRating)
                    .collect(Collectors.toList());
        } else if (query != null && !query.isEmpty() && category != null) {
            places = placeRepository.findByNameContainingIgnoreCaseAndCategoryName(query, category);
        } else if (query != null && !query.isEmpty() && minRating != null) {
            places = placeRepository.findByNameContainingIgnoreCaseAndAvgRatingGreaterThanEqual(query, minRating);
        } else if (query != null && !query.isEmpty()) {
            places = placeRepository.findByNameContainingIgnoreCase(query);
        } else if (category != null) {
            places = placeRepository.findByCategoryName(category);
        } else if (minRating != null) {
            places = placeRepository.findByAvgRatingGreaterThanEqual(minRating);
        } else {
            places = placeRepository.findAll();
        }

        return places.stream()
                .filter(place -> {
                    if (request.getMinTimeSpent() != null && place.getAvgTimeSpent() != null) {
                        return place.getAvgTimeSpent() >= request.getMinTimeSpent();
                    }
                    return true;
                })
                .filter(place -> {
                    if (request.getMaxTimeSpent() != null && place.getAvgTimeSpent() != null) {
                        return place.getAvgTimeSpent() <= request.getMaxTimeSpent();
                    }
                    return true;
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PlaceDTO getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));
        return convertToDTO(place);
    }

    @Transactional
    public PlaceDTO updateTimeSpent(Long placeId, Double timeSpent) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + placeId));
        place.setAvgTimeSpent(timeSpent);
        place = placeRepository.save(place);
        return convertToDTO(place);
    }

    @Transactional
    public void loadPlacesFromKudaGo() {
        List<Place> kudaGoPlaces = kudaGoService.fetchPlaces();
        placeRepository.saveAll(kudaGoPlaces);
    }

    private PlaceDTO convertToDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setCategory(place.getCategory() != null ? place.getCategory().getName() : null);
        dto.setDescription(place.getDescription());
        dto.setImageUrl(place.getImageUrl());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setAddress(place.getAddress());
        dto.setAvgRating(place.getAvgRating());
        dto.setAvgTimeSpent(place.getAvgTimeSpent());
        dto.setAvgTimeSpentFormatted(formatHours(place.getAvgTimeSpent()));

        List<ReviewDTO> reviewDTOs = place.getReviews().stream()
                .map(this::convertReviewToDTO)
                .collect(Collectors.toList());
        dto.setReviews(reviewDTOs);

        return dto;
    }

    private ReviewDTO convertReviewToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserName(review.getUserName());
        dto.setText(review.getText());
        dto.setRating(review.getRating());
        dto.setCreatedAt(review.getCreatedAt() != null ? review.getCreatedAt().toString() : null);
        return dto;
    }

    private String formatHours(Double hours) {
        if (hours == null) return "1ч 0м";
        int h = (int) Math.floor(hours);
        int m = (int) ((hours - h) * 60);
        return String.format("%dч %dм", h, m);
    }
}