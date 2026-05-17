package ru.optiroute.demo.place;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.optiroute.demo.place.Category;
import ru.optiroute.demo.place.Place;
import ru.optiroute.demo.place.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class KudaGoService {

    private static final Logger log = LoggerFactory.getLogger(KudaGoService.class);

    @Value("${kudago.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public KudaGoService(RestTemplate restTemplate,
                         CategoryRepository categoryRepository) {
        this.restTemplate = restTemplate;
        this.categoryRepository = categoryRepository;
        this.objectMapper = new ObjectMapper();
    }

    public List<Place> fetchPlaces() {
        List<Place> places = new ArrayList<>();

        try {
            String categoriesUrl = baseUrl + "/place-categories/";
            String categoriesResponse = restTemplate.getForObject(categoriesUrl, String.class);
            JsonNode categoriesNode = objectMapper.readTree(categoriesResponse);

            Map<String, Category> categoryMap = new HashMap<>();
            for (JsonNode categoryNode : categoriesNode) {
                Category category = new Category();
                category.setName(categoryNode.get("name").asText());
                category.setKudagoSlug(categoryNode.get("slug").asText());
                category = categoryRepository.save(category);
                categoryMap.put(category.getKudagoSlug(), category);
            }

            String placesUrl = baseUrl + "/places/?page_size=100&fields=id,title,description,coords,address,images,categories";
            String placesResponse = restTemplate.getForObject(placesUrl, String.class);
            JsonNode placesNode = objectMapper.readTree(placesResponse);
            JsonNode results = placesNode.get("results");

            if (results != null) {
                for (JsonNode placeNode : results) {
                    Place place = new Place();
                    place.setKudagoId(placeNode.get("id").asLong());
                    place.setName(placeNode.get("title").asText());

                    if (placeNode.has("description")) {
                        place.setDescription(placeNode.get("description").asText());
                    }

                    JsonNode coords = placeNode.get("coords");
                    if (coords != null) {
                        place.setLatitude(coords.get("lat").asDouble());
                        place.setLongitude(coords.get("lon").asDouble());
                    }

                    if (placeNode.has("address")) {
                        place.setAddress(placeNode.get("address").asText());
                    }

                    JsonNode images = placeNode.get("images");
                    if (images != null && images.size() > 0) {
                        place.setImageUrl(images.get(0).get("image").asText());
                    }

                    JsonNode categories = placeNode.get("categories");
                    if (categories != null && categories.size() > 0) {
                        String categorySlug = categories.get(0).asText();
                        Category category = categoryMap.get(categorySlug);
                        if (category == null) {
                            category = new Category();
                            category.setName(categorySlug);
                            category.setKudagoSlug(categorySlug);
                            category = categoryRepository.save(category);
                            categoryMap.put(categorySlug, category);
                        }
                        place.setCategory(category);
                    }

                    places.add(place);
                }
            }

        } catch (Exception e) {
            log.error("Error fetching data from KudaGo API", e);
        }

        return places;
    }
}