package ru.optiroute.demo.place;

import ru.optiroute.demo.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCategoryName(String categoryName);

    List<Place> findByCity(String city);  // НОВЫЙ МЕТОД

    List<Place> findByCityAndCategoryName(String city, String categoryName);  // НОВЫЙ МЕТОД

    List<Place> findByNameContainingIgnoreCase(String name);

    List<Place> findByNameContainingIgnoreCaseAndCity(String name, String city);  // НОВЫЙ МЕТОД

    List<Place> findByNameContainingIgnoreCaseAndCategoryName(String name, String categoryName);

    List<Place> findByNameContainingIgnoreCaseAndCategoryNameAndCity(String name, String categoryName, String city);  // НОВЫЙ МЕТОД

    List<Place> findByNameContainingIgnoreCaseAndAvgRatingGreaterThanEqual(String name, Double minRating);

    List<Place> findByNameContainingIgnoreCaseAndAvgRatingGreaterThanEqualAndCity(String name, Double minRating, String city);  // НОВЫЙ МЕТОД

    List<Place> findByAvgRatingGreaterThanEqual(Double minRating);

    List<Place> findByAvgRatingGreaterThanEqualAndCity(Double minRating, String city);  // НОВЫЙ МЕТОД

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