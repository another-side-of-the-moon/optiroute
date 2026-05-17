package ru.optiroute.demo.place;

import ru.optiroute.demo.place.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCategoryName(String categoryName);

    @EntityGraph(attributePaths = {"reviews", "category"})
    List<Place> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"reviews", "category"})
    List<Place> findByNameContainingIgnoreCaseAndCategoryName(String name, String categoryName);

    @EntityGraph(attributePaths = {"reviews", "category"})
    List<Place> findByNameContainingIgnoreCaseAndAvgRatingGreaterThanEqual(String name, Double minRating);

    @EntityGraph(attributePaths = {"reviews", "category"})
    List<Place> findByAvgRatingGreaterThanEqual(Double minRating);

    @EntityGraph(attributePaths = {"reviews", "category"})
    List<Place> findAll();

    @EntityGraph(attributePaths = {"reviews", "category"})
    Optional<Place> findById(Long id);

    List<Place> findByKudagoId(Long kudagoId);

    @Query(value = "SELECT *, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
            "FROM places p " +
            "ORDER BY distance", nativeQuery = true)
    List<Place> findNearbyPlaces(@Param("latitude") Double latitude,
                                 @Param("longitude") Double longitude);
}