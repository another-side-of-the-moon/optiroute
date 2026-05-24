package ru.optiroute.demo.place.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    @Value("${kudago.api.base-url}")
    private String baseUrl;

    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> ALLOWED_SLUGS = CategoryController.ALLOWED_CATEGORIES.stream()
            .map(cat -> (String) cat.get("slug"))
            .collect(Collectors.toSet());

    public CategoryService(CategoryRepository categoryRepository, RestTemplate restTemplate) {
        this.categoryRepository = categoryRepository;
        this.restTemplate = restTemplate;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public void initAllowedCategories() {
        log.info("Initializing allowed categories...");

        for (Map<String, Object> categoryData : CategoryController.ALLOWED_CATEGORIES) {
            String slug = (String) categoryData.get("slug");
            String name = (String) categoryData.get("name");
            Integer kudagoId = (Integer) categoryData.get("id");

            Optional<Category> existingCategory = categoryRepository.findByKudagoSlug(slug);

            if (existingCategory.isEmpty()) {
                Category category = new Category();
                category.setName(name);
                category.setKudagoSlug(slug);

                try {
                    categoryRepository.save(category);
                    log.debug("Created category: {} ({})", name, slug);
                } catch (Exception e) {
                    log.warn("Failed to create category: {} ({})", name, slug, e);
                }
            } else {
                Category category = existingCategory.get();
                if (!category.getName().equals(name)) {
                    category.setName(name);
                    categoryRepository.save(category);
                    log.debug("Updated category name: {} -> {}", slug, name);
                }
            }
        }

        log.info("Allowed categories initialization completed");
    }
}