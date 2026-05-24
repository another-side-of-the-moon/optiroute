package ru.optiroute.demo.place.category;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public static final List<Map<String, Object>> ALLOWED_CATEGORIES = List.of(
            Map.of("id", 89, "slug", "amusement", "name", "Развлечения"),
            Map.of("id", 113, "slug", "art-centers", "name", "Арт-центры"),
            Map.of("id", 130, "slug", "art-space", "name", "Арт-пространства"),
            Map.of("id", 51, "slug", "attractions", "name", "Достопримечательности"),
            Map.of("id", 63, "slug", "church", "name", "Церкви"),
            Map.of("id", 45, "slug", "culture", "name", "Дома культуры"),
            Map.of("id", 69, "slug", "homesteads", "name", "Усадьбы"),
            Map.of("id", 102, "slug", "kids", "name", "Детям"),
            Map.of("id", 66, "slug", "monastery", "name", "Монастыри"),
            Map.of("id", 42, "slug", "museums", "name", "Музеи и галереи"),
            Map.of("id", 67, "slug", "palace", "name", "Дворцы"),
            Map.of("id", 59, "slug", "park", "name", "Парки (Интересные места, Отдых)"),
            Map.of("id", 91, "slug", "photo-places", "name", "Фотоместа (Фотография)"),
            Map.of("id", 137, "slug", "prirodnyj-zapovednik", "name", "Природный заповедник"),
            Map.of("id", 140, "slug", "recreation", "name", "Активный отдых"),
            Map.of("id", 15, "slug", "restaurants", "name", "Рестораны и кафе"),
            Map.of("id", 53, "slug", "sights", "name", "Интересные места"),
            Map.of("id", 70, "slug", "suburb", "name", "Загородный отдых"),
            Map.of("id", 65, "slug", "synagogue", "name", "Синагоги"),
            Map.of("id", 62, "slug", "temple", "name", "Храмы"),
            Map.of("id", 48, "slug", "theatre", "name", "Театры")
    );

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}