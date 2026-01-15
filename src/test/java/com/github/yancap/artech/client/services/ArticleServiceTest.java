package com.github.yancap.artech.client.services;

import com.github.yancap.artech.client.TestSqlScriptExecutor;
import com.github.yancap.artech.client.services.ArticleService;
import com.github.yancap.artech.client.persistence.dto.article.ArticleDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@Transactional
public class ArticleServiceTest {

    @Inject
    TestSqlScriptExecutor sqlScript;

    @Inject
    ArticleService service;

    @BeforeEach
    @Transactional
    void setup(){
        sqlScript.start();
    }

    @Test
    @DisplayName("it should be able to get all articles")
    void getArticles(){
        var articles = service.getArticles();
        articles.stream().forEach((article) -> {
            assertInstanceOf(ArticleDTO.class, article);
            assertEquals("John Doe", article.author().name());
        });
    }

    @Test
    @DisplayName("it should be able to get all articles by category")
    void getArticlesByCategory(){
        var articles = service.getArticlesByCategory("teste 1");
        articles.stream().forEach((article) -> {
            assertInstanceOf(ArticleDTO.class, article);
            assertEquals("teste 1", article.category());
        });
    }

    @Test
    @DisplayName("it should be able to get an article by slug")
    void getArticleBySlug(){
        var article = service.getArticleBySlug("article-example-1");
        assertEquals("Article Example", article.title());
        assertEquals("article example", article.text());
    }

}
