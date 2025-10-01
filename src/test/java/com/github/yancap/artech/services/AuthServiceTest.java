package com.github.yancap.artech.services;

import com.github.yancap.artech.TestSqlScriptExecutor;
import com.github.yancap.artech.persistence.dto.auth.LoginUserRequestDTO;
import com.github.yancap.artech.persistence.models.User;
import com.github.yancap.artech.persistence.repositories.UserRepository;
import com.github.yancap.artech.persistence.repositories.UserRepository;
import com.github.yancap.artech.utils.errors.ArtechException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AuthServiceTest {

    @Inject
    AuthService service;

    @Inject
    UserRepository repository;

    @Inject
    TestSqlScriptExecutor sqlScript;

    @BeforeEach
    @Transactional
    void setup(){
        sqlScript.start();
    }

    @Test
    @DisplayName("it should be able to login manager with his password without hash")
    void loginWithNoHashPassword(){
        LoginUserRequestDTO dto = new LoginUserRequestDTO(
                "usuario@email.com",
                "123"
        );

        var manager = service.login(dto);

        assertEquals(dto.email(), manager.getEmail());
        assertEquals(dto.password(), manager.getPassword());
    }


    @Test
    @DisplayName("it should be able to login manager with his password hash")
    void loginWithHashPassword(){
        var hashPassword = service.generateHashPassword("123");
        var manager = repository.findByEmail("usuario@email.com");
        manager.setPassword(hashPassword);
        repository.persist(manager);

        LoginUserRequestDTO dto = new LoginUserRequestDTO(
                "usuario@email.com",
                "123"
        );

        manager = service.login(dto);

        assertEquals(dto.email(), manager.getEmail());
    }



    @Test
    @DisplayName("it should not be able to login of non-existent manager")
    void loginArtechExceptionManagerNull(){
        LoginUserRequestDTO dto = new LoginUserRequestDTO(
                "non-existent@email.com",
                "123"
        );

        assertThrows(ArtechException.class, () -> {
            service.login(dto);
        });
    }

    @Test
    @DisplayName("it should not be able to login if manager's password is wrong")
    void loginArtechExceptionWrongPassword(){
        LoginUserRequestDTO dto = new LoginUserRequestDTO(
                "usuario@email.com",
                "wrong password"
        );

        assertThrows(ArtechException.class, () -> {
            service.login(dto);
        });
    }


    @Test
    @DisplayName("it should be able to generate token from data manager")
    void generateToken(){
        User user = repository.findByEmail("usuario@email.com");

        String token = service.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("it should be able to validate jwt token and return data manager")
    void validateToken(){

        //Mockando uma interface
        SecurityContext ctxMock = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return "Mock Name";
                    }
                };
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return "";
            }
        };

        JsonWebToken jwtMock = new JsonWebToken() {
            @Override
            public String getName() {
                return "Mock Name";
            }

            @Override
            public Set<String> getClaimNames() {
                return Set.of();
            }

            @Override
            public String getClaim(String s) {

                switch (s) {
                    case "name":
                        return "Usuario";
                    case "email":
                        return "usuario@email.com";
                    case "role":
                        return "Author";
                    case "urlAvatar":
                        return "";
                    default:
                        return null;
                }

            }
        };

        var dto = service.validateToken(ctxMock, jwtMock);
        assertNotNull(dto);
        assertEquals("usuario@email.com", dto.email());
        assertEquals("Usuario", dto.name());
    }

    @Test
    @DisplayName("it should not be able to validate an invalid jwt token from SecurityContext")
    void notValidateTokenIfInvalidSecurityContext(){

        //Mockando uma interface
        SecurityContext ctxMock = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return "";
            }
        };

        JsonWebToken jwtMock = new JsonWebToken() {
            @Override
            public String getName() {
                return "Mock Name";
            }

            @Override
            public Set<String> getClaimNames() {
                return Set.of();
            }

            @Override
            public String getClaim(String s) {

                switch (s) {
                    case "name":
                        return "Usuario";
                    case "email":
                        return "usuario@email.com";
                    case "role":
                        return "Author";
                    case "urlAvatar":
                        return "";
                    default:
                        return null;
                }

            }
        };

        assertThrows(ArtechException.class, () -> {
            service.validateToken(ctxMock, jwtMock);
        });
    }
    @Test
    @DisplayName("it should not be able to validate  jwt token if jwt name is wrong")
    void notValidateTokenIfJwtNameWrong(){

        //Mockando uma interface
        SecurityContext ctxMock = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return "Mock Name";
                    }
                };
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return "";
            }
        };

        JsonWebToken jwtMock = new JsonWebToken() {
            @Override
            public String getName() {
                return "Wrong Name";
            }

            @Override
            public Set<String> getClaimNames() {
                return Set.of();
            }

            @Override
            public String getClaim(String s) {

                switch (s) {
                    case "name":
                        return "Usuario";
                    case "email":
                        return "usuario@email.com";
                    case "role":
                        return "Author";
                    case "urlAvatar":
                        return "";
                    default:
                        return null;
                }

            }
        };

        assertThrows(ArtechException.class, () -> {
            service.validateToken(ctxMock, jwtMock);
        });
    }



}
