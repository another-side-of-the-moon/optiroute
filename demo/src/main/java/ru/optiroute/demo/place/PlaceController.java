package ru.optiroute.demo.place;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.optiroute.demo.place.category.CategoryController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/places")
@CrossOrigin(origins = "*")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> searchPlaces(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String city,  // НОВЫЙ ПАРАМЕТР
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double minTimeSpent,
            @RequestParam(required = false) Double maxTimeSpent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PlaceSearchRequest request = new PlaceSearchRequest();
        request.setQuery(query);

        if (categories != null && !categories.isEmpty()) {
            List<String> decodedCategories = new ArrayList<>();
            for (String cat : categories) {
                try {
                    decodedCategories.add(URLDecoder.decode(cat, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    decodedCategories.add(cat);
                }
            }
            request.setCategories(decodedCategories);
        }

        if (city != null && !city.isEmpty()) {
            try {
                city = URLDecoder.decode(city, StandardCharsets.UTF_8);
            } catch (Exception e) {
            }
        }
        request.setCity(city);

        request.setMinRating(minRating);
        request.setMinTimeSpent(minTimeSpent);
        request.setMaxTimeSpent(maxTimeSpent);
        request.setPage(page);
        request.setSize(size);

        return ResponseEntity.ok(placeService.searchPlaces(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @PutMapping("/{id}/time-spent")
    public ResponseEntity<PlaceDTO> updateTimeSpent(
            @PathVariable Long id,
            @RequestParam Double timeSpent) {
        return ResponseEntity.ok(placeService.updateTimeSpent(id, timeSpent));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        return ResponseEntity.ok(placeService.getCities());
    }
    @PostMapping("/load-from-kudago")
    public ResponseEntity<String> loadPlacesFromKudaGo() {
        placeService.loadPlacesFromKudaGo();
        return ResponseEntity.ok("Places loaded successfully for allowed categories");
    }

    @GetMapping("/allowed-categories")
    public ResponseEntity<List<Map<String, Object>>> getAllowedCategories() {
        return ResponseEntity.ok(CategoryController.ALLOWED_CATEGORIES);
    }
}
