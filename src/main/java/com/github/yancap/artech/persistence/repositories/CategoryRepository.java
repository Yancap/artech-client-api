package com.github.yancap.artech.persistence.repositories;

import com.github.yancap.artech.persistence.dto.article.ArticleEntityDTO;
import com.github.yancap.artech.persistence.dto.category.CategoryEntityDTO;
import com.github.yancap.artech.persistence.models.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public List<ArticleEntityDTO> queryEngine(String queryParam) {
        var slicedQueryParam = queryParam.split(" ");
        var categoryQuery = "category LIKE ";
        for (int i = 0; i < slicedQueryParam.length; i++){
            categoryQuery = categoryQuery + "'%" + slicedQueryParam[i] + "%'";
            if (i != (slicedQueryParam.length - 1)) {
                categoryQuery = categoryQuery + " OR category LIKE ";
            }
        }
        var result = getEntityManager()
                .createNativeQuery("SELECT * FROM categories WHERE  "
                        + categoryQuery + ";", CategoryEntityDTO.class
                )
                .getResultList();
        List<List<ArticleEntityDTO>> listofArticleList = result.stream()
        .map((Object object) -> {
            var categoryEntity = (CategoryEntityDTO) object;
            List<ArticleEntityDTO> articleList = new ArrayList<>();
            for (ArticleEntityDTO article : categoryEntity.getArticles()) {
                articleList.add(article);
            }
            return articleList;

        }).toList();
        List<ArticleEntityDTO> articleFilteredFromQueryCategory = new ArrayList<>();
        for(List<ArticleEntityDTO> articleList: listofArticleList) {
            for(ArticleEntityDTO article: articleList) {
                articleFilteredFromQueryCategory.add(article);
            }
        }

        return articleFilteredFromQueryCategory;
    }

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
