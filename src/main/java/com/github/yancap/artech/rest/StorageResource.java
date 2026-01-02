package com.github.yancap.artech.rest;

import com.github.yancap.artech.services.ImageStorageService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;


import java.io.File;

@Path("storage/")
@Produces({"image/png", "image/jpg", "image/jpeg"})
//@Produces(MediaType.TEXT_PLAIN)
public class StorageResource {

    @Inject
    ImageStorageService imageStorageService;

    @GET
    @Path("images/users/{pathImage}")
    @Transactional
    public byte[] usersImage(
            @PathParam("pathImage") String pathImage
    ) {
        try {
            String currentDir = new File("").getAbsolutePath();
            System.out.println("currentDir: " + currentDir);
            String storageDir = new File(currentDir + "/storage/images/users/").getAbsolutePath();
            System.out.println("storageDir: " + storageDir);
            String imagePath = new File(storageDir + "\\" + pathImage).getCanonicalPath();
            System.out.println("imagePath: " + imagePath);

            return imageStorageService.readImage("/images/users/" + pathImage);
        } catch (Throwable e) {

            return null;
        }
    }



}
