package ru.optiroute.demo.place;

import ru.optiroute.demo.place.review.ReviewDTO;
import ru.optiroute.demo.place.review.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final KudaGoService kudaGoService;

    public PlaceService(PlaceRepository placeRepository, KudaGoService kudaGoService) {
        this.placeRepository = placeRepository;
        this.kudaGoService = kudaGoService;
    }

    public List<PlaceDTO> searchPlaces(PlaceSearchRequest request) {
        List<Place> places = new ArrayList<>();

        String query = request.getQuery();
        String category = request.getCategories() != null && !request.getCategories().isEmpty()
                ? request.getCategories().get(0) : null;
        String city = request.getCity();  // Получаем город
        Double minRating = request.getMinRating();

        if (city == null || city.isEmpty()) {
            return new ArrayList<>();
        }

        if (query != null && !query.isEmpty() && category != null && minRating != null) {
            // Все параметры + город
            places = placeRepository.findByNameContainingIgnoreCaseAndCategoryNameAndCity(query, category, city)
                    .stream()
                    .filter(p -> p.getAvgRating() != null && p.getAvgRating() >= minRating)
                    .collect(Collectors.toList());
        } else if (query != null && !query.isEmpty() && category != null) {
            places = placeRepository.findByNameContainingIgnoreCaseAndCategoryNameAndCity(query, category, city);
        } else if (query != null && !query.isEmpty() && minRating != null) {
            places = placeRepository.findByNameContainingIgnoreCaseAndAvgRatingGreaterThanEqualAndCity(query, minRating, city);
        } else if (category != null && minRating != null) {
            places = placeRepository.findByCityAndCategoryName(city, category)
                    .stream()
                    .filter(p -> p.getAvgRating() != null && p.getAvgRating() >= minRating)
                    .collect(Collectors.toList());
        } else if (query != null && !query.isEmpty()) {
            places = placeRepository.findByNameContainingIgnoreCaseAndCity(query, city);
        } else if (category != null) {
            places = placeRepository.findByCityAndCategoryName(city, category);
        } else if (minRating != null) {
            places = placeRepository.findByAvgRatingGreaterThanEqualAndCity(minRating, city);
        } else {
            // Только город
            places = placeRepository.findByCity(city);
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
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlaceDTO getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));
        return convertToDetailDTO(place);
    }

    @Transactional
    public PlaceDTO updateTimeSpent(Long placeId, Double timeSpent) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + placeId));
        place.setAvgTimeSpent(timeSpent);
        place = placeRepository.save(place);
        return convertToDetailDTO(place);
    }

    @Transactional
    public void loadPlacesFromKudaGo() {
        List<Place> kudaGoPlaces = kudaGoService.fetchPlaces();
        placeRepository.saveAll(kudaGoPlaces);
    }

    public List<String> getCities() {
        return placeRepository.findAll().stream()
                .map(Place::getCity)
                .filter(city -> city != null && !city.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private PlaceDTO convertToListDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setCategory(place.getCategory() != null ? place.getCategory().getName() : null);
        dto.setDescription(place.getDescription());
        dto.setImageUrl(place.getImageUrl());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setAddress(place.getAddress());
        dto.setCity(place.getCity());  // Добавляем город
        dto.setAvgRating(place.getAvgRating());
        dto.setAvgTimeSpent(place.getAvgTimeSpent());
        dto.setAvgTimeSpentFormatted(formatHours(place.getAvgTimeSpent()));
        dto.setReviews(new ArrayList<>());
        return dto;
    }

    private PlaceDTO convertToDetailDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setCategory(place.getCategory() != null ? place.getCategory().getName() : null);
        dto.setDescription(place.getDescription());
        dto.setImageUrl(place.getImageUrl());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setAddress(place.getAddress());
        dto.setCity(place.getCity());  // Добавляем город
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