package com.github.yancap.artech.client.services;

import com.github.yancap.artech.client.services.ImageStorageService;
import com.github.yancap.artech.client.utils.errors.ArtechException;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageStorageServiceTest {

    @Inject
    ImageStorageService service;

    static String imagePath;


    @Test
    @Order(1)
    @Transactional
    @DisplayName("it should be able to upload image in storage")
    void uploadImage(){
        var imageBase64 = "data:image/png;base64,R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs=";
        var serverPath = service.uploadImage(imageBase64, "images/users/");
        imagePath = serverPath;
        assertNotNull(serverPath);
        assertTrue(serverPath.contains("/storage/images/users/"));
    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("it should not be able to upload image in storage if path not exist")
    void notUploadImageIfPathNotExist(){
        var imageBase64 = "data:image/png;base64,R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs=";

        var serverPath =  service.uploadImage(imageBase64, "images/non/exist/");

        imagePath = serverPath;
        assertNotNull(serverPath);
        assertTrue(serverPath.contains("/storage/images/non/exist/"));
    }


    @Test
    @Order(3)
    @Transactional
    @DisplayName("it should not be able to upload image in storage if base64 image is invalid")
    void notUploadImageIfPathNotExist2(){
        var imageBase64 = "data:image/png;base64,GASD3=";

        var exception =
                assertThrows(ArtechException.class, () -> service.uploadImage(imageBase64, "images/users/"));

        assertEquals(500, exception.getStatus());
        assertEquals("Erro de Sistema: Erro de IO no upload da imagem", exception.getMessage());
    }


    @Test
    @Order(4)
    @Transactional
    @DisplayName("it should  be able to read image")
    void readImage(){
        var path = imagePath.split("/storage/")[1];
        var bytes = service.readImage(path);
        assertNotNull(bytes);
    }


    @Test
    @Order(5)
    @Transactional
    @DisplayName("it should not be able to read image if path not exist")
    void notReadImageIfPathNotExist(){
        var exception = assertThrows(ArtechException.class, () -> service.readImage("/not/exist"));
        assertEquals("IIOException", exception.getError());
        assertEquals("Can't read input file!", exception.getDetails());
    }



    @Test
    @Transactional
    @DisplayName("it should be able to get image extension from Base64")
    void getImageExtensionForBase64(){
        var imageBase64 = "data:image/png;base64,R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs=";
        var imageExtension = service.getImageExtensionFromBase64(imageBase64);
        assertNotNull(imageExtension);
        assertEquals("png", imageExtension);
    }


    @Test
    @Transactional
    @DisplayName("it should be able to throw error if string is not base64 structure")
    void errorToGetImageExtensionForBase64(){
        var imageBase64 = ",R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs=";
        var exception = assertThrows(
                ArtechException.class,
                () -> service.getImageExtensionFromBase64(imageBase64)
        );

        assertEquals(422, exception.getStatus());
        assertEquals("Erro negocial: verifique na documetação da API.", exception.getMessage());
    }


}
