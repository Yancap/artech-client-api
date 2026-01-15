package com.github.yancap.artech.client.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yancap.artech.client.TestSqlScriptExecutor;
import com.github.yancap.artech.client.rest.CommentResource;
import com.github.yancap.artech.client.persistence.dto.comments.ArticleCommentResponseDTO;
import com.github.yancap.artech.client.persistence.dto.comments.CreateCommentRequestDTO;
import com.github.yancap.artech.client.persistence.repositories.CommentRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@Transactional
@TestHTTPEndpoint(CommentResource.class)
public class CommentsResourceTest {


    @Inject
    CommentRepository repository;

    @Inject
    TestSqlScriptExecutor sqlScript;


    @BeforeEach
    void setup(){
        sqlScript.start();
    }

    @Test
    @DisplayName("it should be able to create an comment")
    @TestSecurity(
            user = "testUser",
            roles = "User"
    )
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "usuario@email.com"),
            @Claim(key = "name", value = "Usuario")
    })
    void createComment(){
        CreateCommentRequestDTO dto = new CreateCommentRequestDTO(
                "article-example-1",
                "Comentario 1"
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(201);

        var comment = repository.find("text", "Comentario 1");
        assertNotNull(comment);
    }
    @Test
    @DisplayName("it should not be able to create an comment if user not exist")
    @TestSecurity(
            user = "testUser",
            roles = "User"
    )
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "nonExist@email.com"),
            @Claim(key = "name", value = "Not Exist")
    })
    void dontCreateCommentIfUserNotExist(){
        CreateCommentRequestDTO dto = new CreateCommentRequestDTO(
                "article-example-1",
                "Comentario 1"
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("it should not be able to create an comment if article not exist")
    @TestSecurity(
            user = "testUser",
            roles = "User"
    )
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "usuario@email.com"),
            @Claim(key = "name", value = "Usuario")
    })
    void dontCreateCommentIfArticleNotExist(){
        CreateCommentRequestDTO dto = new CreateCommentRequestDTO(
                "not-exist-example-1",
                "Comentario 1"
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("it should be able to delete an article comment")
    @TestSecurity(
            user = "testUser",
            roles = "User"
    )
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "usuario@email.com"),
            @Claim(key = "name", value = "Usuario")
    })
    void deleteComment(){

        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("100")
        .then()
            .statusCode(204);

        var comment = repository.findById(100L);
        assertNull(comment);
    }

    @Test
    @DisplayName("it should not be able to delete an article comment")
    @TestSecurity(
            user = "testUser",
            roles = "User"
    )
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "otherUser@email.com"),
            @Claim(key = "name", value = "Other")
    })
    void dontDeleteOtherUserComment(){

        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("100")
        .then()
            .statusCode(404);

        var comment = repository.findById(100L);
        assertNotNull(comment);
    }
}
