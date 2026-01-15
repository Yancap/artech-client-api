package com.github.yancap.artech.client.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yancap.artech.client.TestSqlScriptExecutor;
import com.github.yancap.artech.client.rest.ArticleResource;
import com.github.yancap.artech.client.persistence.dto.article.ArticleDTO;
import com.github.yancap.artech.client.persistence.dto.article.ArticleEntityDTO;
import com.github.yancap.artech.client.persistence.dto.article.ArticleResponseDTO;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
@TestHTTPEndpoint(ArticleResource.class)
public class ArticleResourceTest {

    @Inject
    TestSqlScriptExecutor sqlScript;

    @BeforeEach
    void setup(){
        sqlScript.start();
    }

    @Test
    @DisplayName("it should be able to get all active articles")
    void getArticles(){
        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .<List<ArticleDTO>>get("articlesList");

        ObjectMapper mapper = new ObjectMapper();
        List<ArticleDTO> articles = mapper.convertValue(
                response,
                new TypeReference<List<ArticleDTO>>(){}
        );
        assertNotNull(articles);
        articles.forEach(article -> {
            assertEquals("active", article.currentState());
        });
    }

    @Test
    @DisplayName("it should be able to get article by slug")
    void getArticleBySlug(){
        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("article-example-1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .<Object>get("article");

        ObjectMapper mapper = new ObjectMapper();
        ArticleDTO article = mapper.convertValue(
                response,
                new TypeReference<ArticleDTO>(){}
        );
        assertNotNull(article);
        assertEquals("active", article.currentState());
        assertEquals("Article Example", article.title());
    }

    @Test
    @DisplayName("it should not be able to get article by slug if not exist")
    void dontGetArticleBySlugIfNotExist(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("not-exist-example-1")
                .then()
                .statusCode(404);
    }


    @Test
    @DisplayName("it should be able to search all articles by query")
    void searchArticlesByQueryEngine(){
        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .queryParam("q", "Node js")
                .get("search")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .<ArrayList<ArticleEntityDTO>>get();

        assertNotNull(response);
        assertEquals(2, response.size());
    }
    
    @Test
    @DisplayName("it should be able to search all articles by hashtag query")
    void searchArticlesByHashtagQueryEngine(){
        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .queryParam("hashtags", "node,js")
                .get("search")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .<ArrayList<ArticleEntityDTO>>get();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("it should not be able to search articles without query param")
    void dontSearchArticlesByWithAnyParameter(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("search")
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("it should be able to get all articles by category")
    void getArticlesByCategory(){
        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("category/teste 1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .<List<ArticleDTO>>get("articlesList");

        ObjectMapper mapper = new ObjectMapper();
        List<ArticleDTO> articles = mapper.convertValue(
                response,
                new TypeReference<List<ArticleDTO>>(){}
        );
        assertNotNull(articles);
        articles.forEach(article -> {
            assertEquals("active", article.currentState());
            assertEquals("teste 1", article.category());
        });
    }

}
