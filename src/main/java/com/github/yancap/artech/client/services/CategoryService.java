package com.github.yancap.artech.client.services;

import com.github.yancap.artech.client.persistence.dto.category.CategoryDTO;
import com.github.yancap.artech.client.persistence.models.Category;
import com.github.yancap.artech.client.persistence.repositories.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CategoryService {

    @Inject
    private CategoryRepository categoryRepository;

    public CategoryDTO getAll(){
        var categories = categoryRepository.listAll();
        return new CategoryDTO(categories.stream().map((Category::getCategory)).toList());
    }
}
