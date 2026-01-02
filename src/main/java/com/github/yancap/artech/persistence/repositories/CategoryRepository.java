package com.github.yancap.artech.persistence.repositories;

import com.github.yancap.artech.persistence.models.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {


    public Category findByCategoryName(String categoryName) {
        return find("category", categoryName.toLowerCase()).firstResult();
    }

    public Category persistIfNotExist(String category) {
        var currentCategory = find("category", category.toLowerCase()).firstResult();
        if(currentCategory == null) {
            Category categoryEntity = new Category();
            categoryEntity.setCategory(category);
            persist(categoryEntity);
            return categoryEntity;
        }
        return currentCategory;
    }
}
