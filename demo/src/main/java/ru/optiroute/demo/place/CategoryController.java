package ru.optiroute.demo.place;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @GetMapping("/time-recommendations")
    public ResponseEntity<Map<String, Double>> getTimeRecommendations() {
        Map<String, Double> recommendations = new LinkedHashMap<>();
        recommendations.put("музей/museum", 3.0);
        recommendations.put("парк/park", 1.5);
        recommendations.put("ресторан/restaurant", 1.5);
        recommendations.put("кафе/cafe", 1.5);
        recommendations.put("театр/theatre", 2.5);
        recommendations.put("кино/cinema", 2.0);
        recommendations.put("галерея/gallery", 2.0);
        recommendations.put("зоопарк/zoo", 3.0);
        recommendations.put("магазин/shop", 1.5);
        recommendations.put("храм/church", 1.5);
        recommendations.put("выставка/exhibition", 1.5);
        return ResponseEntity.ok(recommendations);
    }
}
