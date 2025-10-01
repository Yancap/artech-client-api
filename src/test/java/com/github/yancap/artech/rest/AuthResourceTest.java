package com.github.yancap.artech.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yancap.artech.TestSqlScriptExecutor;
import com.github.yancap.artech.persistence.dto.auth.LoginUserRequestDTO;
import com.github.yancap.artech.persistence.dto.auth.UserResponseDTO;
import com.github.yancap.artech.persistence.repositories.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@Transactional
@TestHTTPEndpoint(AuthResource.class)
public class AuthResourceTest {

    @Inject
    UserRepository repository;

    @Inject
    TestSqlScriptExecutor sqlScript;

    @BeforeEach
    void setup(){
        sqlScript.start();
    }

    @Test
    @DisplayName("it should be able to authenticate itself and it must return token")
    void authentication(){
        var dto = new LoginUserRequestDTO("usuario@email.com", "123");
        var token = given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when()
            .post("login/authentication")
        .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .<String>get("token");

        assertNotNull(token);
    }

    @Test
    @DisplayName("it should not be able to authenticate if email is wrong")
    void notAuthenticationIfEmailWrong(){
        var dto = new LoginUserRequestDTO("wrong@email.com", "123");
        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when()
            .post("login/authentication")
        .then()
            .statusCode(401);

    }


    @Test
    @DisplayName("it should not be able to authenticate if password is wrong")
    void notAuthenticationIfPasswordWrong(){
        var dto = new LoginUserRequestDTO("johndoe@email.com", "wrong");
        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when()
            .post("login/authentication")
        .then()
            .statusCode(401);

    }


    @Test
    @DisplayName("it should be able to send token and it must return user data")
    @TestSecurity(
        user = "John Doe",
        roles = "User"
    )
    @JwtSecurity(claims = {
        @Claim(key = "email", value = "usuario@email.com"),
        @Claim(key = "name", value = "Usuario"),
        @Claim(key = "role", value = "User"),
        @Claim(key = "urlAvatar", value = "")
    })
    void access(){
        var response = given()
            .contentType(ContentType.JSON)
        .when()
            .get("login/access")
        .then()
            .statusCode(200)
            .extract()
            .body()
            .jsonPath()
            .get();

        ObjectMapper mapper = new ObjectMapper();
        UserResponseDTO userDTO = mapper.convertValue(
                response,
                new TypeReference<UserResponseDTO>(){}
        );

        var user = repository.findByEmail("usuario@email.com");
        assertEquals(userDTO.name(), user.getName());
        assertEquals(userDTO.email(), user.getEmail());
    }


    @Test
    @DisplayName("it should not be able to send a invalid token and return user data")
    void notAccessIfTokenIsInvalid(){
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer invalidToken12345abcd")
        .when()
            .get("login/access")
        .then()
            .statusCode(401);

    }



}
