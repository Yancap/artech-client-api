package com.github.yancap.artech.rest;

import com.github.yancap.artech.TestSqlScriptExecutor;
import com.github.yancap.artech.persistence.dto.user.ChangeAvatarRequestDTO;
import com.github.yancap.artech.persistence.dto.user.ChangePasswordRequestDTO;
import com.github.yancap.artech.persistence.dto.user.UserRegisterDTO;
import com.github.yancap.artech.persistence.repositories.UserRepository;
import com.github.yancap.artech.services.AuthService;
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
@TestHTTPEndpoint(UserResource.class)
public class UserResourceTest {

    @Inject
    TestSqlScriptExecutor sqlScript;

    @Inject
    UserRepository repository;


    @Inject
    AuthService authService;

    @BeforeEach
    void setup(){
        sqlScript.start();
    }

    @Test
    @DisplayName("it should be able to register an user")
    void registerUser(){
        UserRegisterDTO dto = new UserRegisterDTO(
                "John User",
                "johnuser@email.com",
                "123",
                "data:image/png;base64,R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs="
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("register")
                .then()
                .statusCode(201);

        var user = repository.findByEmail("johnuser@email.com");
        assertNotNull(user);
    }

    @Test
    @DisplayName("it should be able to change user avatar")
    @TestSecurity(
            user = "testUser",
            roles = "User"
    )
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "usuario@email.com"),
            @Claim(key = "name", value = "Usuario")
    })
    void changeUserAvatar(){
        var dto = new ChangeAvatarRequestDTO(
                "data:image/png;base64,R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs="
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put("change/avatar")
                .then()
                .statusCode(204);

        var user = repository.findByEmail("usuario@email.com");
        assertTrue(user.getUrlAvatar().contains(".png"));
    }
    @Test
    @DisplayName("it should be able to change user password")
    @TestSecurity(
            user = "testUser",
            roles = "User"
    )
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "usuario@email.com"),
            @Claim(key = "name", value = "Usuario")
    })
    void changeUserPassword(){
        var dto = new ChangePasswordRequestDTO(
                "1234567"
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put("change/password")
                .then()
                .statusCode(204);

        var user = repository.findByEmail("usuario@email.com");
        assertTrue(authService.comparePasswordHash("1234567", user.getPassword()));
    }


}
