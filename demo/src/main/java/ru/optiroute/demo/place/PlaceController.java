package ru.optiroute.demo.place;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import ru.optiroute.demo.place.PlaceDTO;
import ru.optiroute.demo.place.PlaceSearchRequest;
import ru.optiroute.demo.place.ReviewDTO;
import ru.optiroute.demo.place.Place;
import ru.optiroute.demo.place.Review;
import ru.optiroute.demo.place.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double minTimeSpent,
            @RequestParam(required = false) Double maxTimeSpent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PlaceSearchRequest request = new PlaceSearchRequest();
        request.setQuery(query);
        request.setCategories(categories);
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

    @PostMapping("/load-from-kudago")
    public ResponseEntity<String> loadPlacesFromKudaGo() {
        placeService.loadPlacesFromKudaGo();
        return ResponseEntity.ok("Places loaded successfully");
    }
}