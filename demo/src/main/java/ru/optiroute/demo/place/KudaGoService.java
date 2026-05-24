package ru.optiroute.demo.place;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.optiroute.demo.place.category.CategoryController;
import ru.optiroute.demo.place.category.Category;
import ru.optiroute.demo.place.category.CategoryRepository;
import ru.optiroute.demo.place.category.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KudaGoService {

    private static final Logger log = LoggerFactory.getLogger(KudaGoService.class);

    @Value("${kudago.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final PlaceRepository placeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> ALLOWED_CATEGORY_SLUGS = CategoryController.ALLOWED_CATEGORIES.stream()
            .map(cat -> (String) cat.get("slug"))
            .collect(Collectors.toSet());

    public KudaGoService(RestTemplate restTemplate,
                         CategoryRepository categoryRepository,
                         CategoryService categoryService,
                         PlaceRepository placeRepository) {
        this.restTemplate = restTemplate;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.placeRepository = placeRepository;
    }

    public List<Place> fetchPlaces() {
        List<Place> allPlaces = new ArrayList<>();

        try {

            categoryService.initAllowedCategories();

            Map<String, Category> categoryMap = new HashMap<>();
            List<Category> allCategories = categoryRepository.findAll();
            for (Category category : allCategories) {
                if (category.getKudagoSlug() != null && ALLOWED_CATEGORY_SLUGS.contains(category.getKudagoSlug())) {
                    categoryMap.put(category.getKudagoSlug(), category);
                }
            }

            log.info("Loaded {} allowed categories for mapping", categoryMap.size());

            String[] cities = {"spb", "msk", "kzn", "ekb", "nnv"};

            for (String categorySlug : ALLOWED_CATEGORY_SLUGS) {
                Category category = categoryMap.get(categorySlug);
                if (category == null) {
                    log.warn("Category with slug '{}' not found in database", categorySlug);
                    continue;
                }

                log.info("Fetching places for category: {} ({})", category.getName(), categorySlug);

                for (String cityCode : cities) {
                    try {
                        List<Place> categoryPlaces = fetchPlacesForCategoryAndCity(categorySlug, category, cityCode);
                        allPlaces.addAll(categoryPlaces);
                    } catch (Exception e) {
                        log.error("Error loading places for category {} in city {}", categorySlug, cityCode, e);
                    }
                }
            }

            if (!allPlaces.isEmpty()) {
                placeRepository.saveAll(allPlaces);
                log.info("Total places saved: {}", allPlaces.size());
            }

        } catch (Exception e) {
            log.error("Error fetching places from KudaGo API", e);
        }

        return allPlaces;
    }

    private List<Place> fetchPlacesForCategoryAndCity(String categorySlug, Category category, String cityCode) {
        List<Place> places = new ArrayList<>();
        String cityName = getCityName(cityCode);
        int page = 1;
        boolean hasMore = true;
        int maxPlacesPerCategoryCity = 200;

        while (hasMore && places.size() < maxPlacesPerCategoryCity) {
            String placesUrl = baseUrl + "/places/" +
                    "?page=" + page +
                    "&page_size=50" +
                    "&location=" + cityCode +
                    "&categories=" + categorySlug +
                    "&fields=id,title,description,coords,address,images,categories";

            log.debug("Fetching: {}", placesUrl);

            try {
                String placesResponse = restTemplate.getForObject(placesUrl, String.class);
                JsonNode placesNode = objectMapper.readTree(placesResponse);
                JsonNode results = placesNode.get("results");

                if (results == null || !results.isArray() || results.size() == 0) {
                    hasMore = false;
                    break;
                }

                for (JsonNode placeNode : results) {
                    Long kudagoId = placeNode.get("id").asLong();

                    // Пропускаем дубликаты
                    if (!placeRepository.findByKudagoId(kudagoId).isEmpty()) {
                        continue;
                    }

                    Place place = new Place();
                    place.setKudagoId(kudagoId);
                    place.setName(placeNode.get("title").asText());
                    place.setCategory(category);

                    if (placeNode.has("description") && !placeNode.get("description").isNull()) {
                        place.setDescription(placeNode.get("description").asText());
                    }

                    JsonNode coords = placeNode.get("coords");
                    if (coords != null && !coords.isNull()) {
                        place.setLatitude(coords.get("lat").asDouble());
                        place.setLongitude(coords.get("lon").asDouble());
                    }

                    if (placeNode.has("address") && !placeNode.get("address").isNull()) {
                        place.setAddress(placeNode.get("address").asText());
                    }

                    place.setCity(cityName);

                    JsonNode images = placeNode.get("images");
                    if (images != null && images.isArray() && images.size() > 0) {
                        place.setImageUrl(images.get(0).get("image").asText());
                    }

                    places.add(place);
                }

                page++;

                JsonNode nextPage = placesNode.get("next");
                if (nextPage == null || nextPage.isNull()) {
                    hasMore = false;
                }

                Thread.sleep(300);

            } catch (Exception e) {
                log.error("Error fetching page {} for category {} in city {}: {}",
                        page, categorySlug, cityName, e.getMessage());
                hasMore = false;
            }
        }

        log.info("Loaded {} places for category {} in {}", places.size(), category.getName(), cityName);
        return places;
    }

    private String getCityName(String cityCode) {
        return switch (cityCode) {
            case "msk" -> "Москва";
            case "spb" -> "Санкт-Петербург";
            case "kzn" -> "Казань";
            case "ekb" -> "Екатеринбург";
            case "nnv" -> "Нижний Новгород";
            default -> cityCode;
        };
    }
}