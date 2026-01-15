package com.github.yancap.artech.client.services;

import com.github.yancap.artech.client.TestSqlScriptExecutor;
import com.github.yancap.artech.client.services.AuthService;
import com.github.yancap.artech.client.services.ImageStorageService;
import com.github.yancap.artech.client.services.UserService;
import com.github.yancap.artech.client.utils.errors.ArtechException;
import com.github.yancap.artech.client.persistence.dto.user.ChangeAvatarRequestDTO;
import com.github.yancap.artech.client.persistence.dto.user.ChangePasswordRequestDTO;
import com.github.yancap.artech.client.persistence.dto.user.UserRegisterDTO;
import com.github.yancap.artech.client.persistence.repositories.UserRepository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@QuarkusTest
@Transactional
public class UserServiceTest {

    @Inject
    TestSqlScriptExecutor sqlScript;


    @Inject
    AuthService authService;

    @Inject
    UserService service;

    @Inject
    UserRepository repository;

    @InjectMock
    ImageStorageService mockImageStorageService;

    @BeforeEach
    @Transactional
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
                "teste:blob"
        );

        when(
                mockImageStorageService.uploadImage(anyString(), anyString())
        ).thenReturn("teste.png");
        service.register(dto);
        var userCreated = repository.findByEmail("johnuser@email.com");
        assertNotNull(userCreated);
        assertEquals(userCreated.getName(), dto.name());
        assertEquals(userCreated.getEmail(), dto.email());

    }


    @Test
    @DisplayName("it should be able to register an user even without avatar")
    void registerUserWithoutAvatar(){
        UserRegisterDTO dto = new UserRegisterDTO(
                "John User",
                "johnuser@email.com",
                "123",
                null
        );

        service.register(dto);
        var userCreated = repository.findByEmail("johnuser@email.com");
        assertNotNull(userCreated);
        assertEquals(userCreated.getName(), dto.name());
        assertEquals(userCreated.getEmail(), dto.email());
        assertNotNull(userCreated.getUrlAvatar());

    }


    @Test
    @DisplayName("it should not be able to register an user if exist")
    void dontRegisterUserIfExist(){
        UserRegisterDTO dto = new UserRegisterDTO(
                "Usuario",
                "usuario@email.com",
                "123",
                ""
        );

        assertThrows(ArtechException.class, () -> service.register(dto));

    }

    @Test
    @DisplayName("it should be able to an user change their password")
    void changePassword(){
        var dto = new ChangePasswordRequestDTO(
                "1234567"
        );

        service.changePassword(dto, "usuario@email.com");
        var userChangedPassword = repository.findByEmail("usuario@email.com");
        assertTrue(authService.comparePasswordHash("1234567", userChangedPassword.getPassword()));

    }

    @Test
    @DisplayName("it should be able to an non-existent user change their password")
    void dontChangePasswordOfNonExistentUser(){
        var dto = new ChangePasswordRequestDTO(
                "1234567"
        );

        assertThrows(ArtechException.class, () -> service.changePassword(dto, "notExist@email.com"));

    }

    @Test
    @DisplayName("it should be able to an user change their avatar")
    void changeAvatar(){
        var dto = new ChangeAvatarRequestDTO(
                "data:image/png;base64,R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs="
        );

        service.changeAvatar(dto, "usuario@email.com");
        var userChangedAvatar = repository.findByEmail("usuario@email.com");
        System.out.println(userChangedAvatar.getUrlAvatar());
        assertEquals(userChangedAvatar.getUrlAvatar(), null);

    }

    @Test
    @DisplayName("it should be able to an non-existent user change their avatar")
    void dontChangeAvatarOfNonExistentUser(){
        var dto = new ChangeAvatarRequestDTO(
                "data:image/png;base64,R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs="
        );

        assertThrows(ArtechException.class, () ->
                service.changeAvatar(dto, "notExist@email.com")
        );

    }
}
