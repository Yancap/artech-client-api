package com.github.yancap.artech.services;

import com.github.yancap.artech.persistence.dto.category.CategoryDTO;
import com.github.yancap.artech.persistence.models.Category;
import com.github.yancap.artech.persistence.repositories.CategoryRepository;
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
