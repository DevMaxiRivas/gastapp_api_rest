package com.app.db.seeder.category;

import com.app.model.Category;
import com.app.repository.CategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoriesInitializer {

    private final CategoryRepository repo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void seed() {
        if (repo.count() > 0) return;

        try {
            ClassPathResource resource = new ClassPathResource("db/seeder/categories.json");

            if (!resource.exists()) {
                System.err.println("categories.json not found.");
                return;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                List<Category> categories = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<Category>>() {}
                );

                repo.saveAll(categories);
                System.out.println("Categories inserted.");
            }

        } catch (IOException e) {
            System.err.println("Parsing categories.json error: " + e.getMessage());
        }
    }
}