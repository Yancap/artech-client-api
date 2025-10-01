package com.github.yancap.artech.services;

import com.github.yancap.artech.persistence.dto.article.ArticleDTO;
import com.github.yancap.artech.persistence.models.Management;
import com.github.yancap.artech.persistence.repositories.ArticleRepository;
import com.github.yancap.artech.persistence.repositories.ArticleTagsRepository;
import com.github.yancap.artech.persistence.repositories.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ArticleService {

    @Inject
    private ArticleRepository articleRepository;

    @Inject
    private ArticleTagsRepository articleTagsRepository;

    @Inject
    private CategoryRepository categoryRepository;

    public List<ArticleDTO> getArticles() {
        var articles = articleRepository.listAllActive();
        return articles.stream().map(article -> {
            var tags = articleTagsRepository.getTagsByArticle(article);
            return new ArticleDTO(article, tags);
        }).toList();
    }


    public List<ArticleDTO> getArticlesByCategory(String category) {
        var categoryObj = categoryRepository.findByCategoryName(category);
        var articles = articleRepository.findByCategory(categoryObj);
        return articles.stream().map(article -> {
            var tags = articleTagsRepository.getTagsByArticle(article);
            return new ArticleDTO(article, tags);
        }).toList();
    }


    public ArticleDTO getArticleBySlug(String slug) {
        var article = articleRepository.findBySlug(slug);
        if (article == null) return null;
        var tags = articleTagsRepository.getTagsByArticle(article);
        return new ArticleDTO(article, tags);
    }

}
