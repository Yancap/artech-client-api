package com.github.yancap.artech.services;

import com.github.yancap.artech.TestSqlScriptExecutor;
import com.github.yancap.artech.persistence.dto.article.ArticleEntityDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@Transactional
public class SearchEngineServiceTest {

    @Inject
    TestSqlScriptExecutor sqlScript;

    @Inject
    SearchEngineService service;

    @BeforeEach
    @Transactional
    void setup(){
        sqlScript.start();
    }

    @Test
    @DisplayName("it should be able to search all articles by a query")
    void searchArticle(){
        var query = "node js";
        var articles = service.searchArticle(query);
        assertEquals(2, articles.size());
    }
    @Test
    @DisplayName("it should be able to search all articles by a hashtag")
    void searchArticleByHashtag(){
        var hashtag = "#node#js";
        var articles = service.searchArticleByTag(hashtag);
        assertEquals(2, articles.size());
    }

}
