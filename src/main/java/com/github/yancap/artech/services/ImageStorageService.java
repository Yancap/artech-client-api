package com.github.yancap.artech.services;

//import com.github.yancap.artech.persistence.properties.ImageStorageProperties;

import com.github.yancap.artech.utils.enums.TypeError;
import com.github.yancap.artech.utils.errors.ArtechError;
import com.github.yancap.artech.utils.errors.ArtechException;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class ImageStorageService {

    private final String BASE64_IMAGE_REGEX = "^data:image\\/(jpeg|png|gif|bmp|webp);base64,[A-Za-z0-9+/=]+$";

    @ConfigProperty(name = "application.storage.path")
    String storagePath;

//    public byte[] readImage(String path, String imageName) {
//        var baos = new ByteArrayOutputStream();
//        try {
//
//            String currentDir = new File("").getAbsolutePath();
//            File storageDir = new File(currentDir + storagePath + path);
//            File imagePath = new File(storageDir + "/" + imageName);
//
//            String extension = FilenameUtils.getExtension(imagePath.getCanonicalPath()).toUpperCase();
//
//            BufferedImage image = ImageIO.read(imagePath);
//            ImageIO.write(image, extension, baos);
//        } catch (Throwable e) {
//            return  null;
//        } finally {
//            try {
//                baos.close();
//            } catch (IOException e) {
//                System.out.println("IOException");
//            }
//        }
//        return baos.toByteArray();
//    }

    public byte[] readImage(String completePath) {
        var baos = new ByteArrayOutputStream();
        try {
            String currentDir = new File("").getAbsolutePath();
            File storageDir = new File(currentDir + storagePath + completePath);

            String extension = FilenameUtils.getExtension(completePath).toUpperCase();
            BufferedImage image = ImageIO.read(storageDir);
            ImageIO.write(image, extension, baos);
            baos.close();
        } catch (Throwable e) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro de Sistema: olhe os Logs do serviço");
            error.setDetails(e.getMessage());
            error.setError(e.getClass().getSimpleName());
            error.setStatus(422);
            throw new ArtechException(error);
        }

        return baos.toByteArray();
    }

    public String uploadImage(String imageBase64, String path) {
        try {
            String extension = getImageExtensionFromBase64(imageBase64);
            UUID uuid = UUID.randomUUID();
            String randomUidName =  uuid.toString();

            String currentDir = new File("").getAbsolutePath();
            File storageDir = new File(currentDir + storagePath + path);
            File imagePath = new File(storageDir + "/" + randomUidName + "." + extension);
            String serverPath = "/storage/" + path + randomUidName + "." + extension;

            byte[] decodedImageBlob = Base64.getDecoder().decode(
                    extractBase64(imageBase64).getBytes(StandardCharsets.UTF_8)
            );

            File file = new File(storageDir.getAbsolutePath());

            if (!file.exists()) {
                file.mkdirs();
            }
            

            FileImageOutputStream imageOutput;
            imageOutput = new FileImageOutputStream(imagePath);
            imageOutput.write(decodedImageBlob, 0, decodedImageBlob.length);
            imageOutput.close();
            return serverPath;
        } catch (FileNotFoundException e) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro de Sistema: Storage não encontrado");
            error.setDetails("Verifique se o caminho do storage está correto na API");
            error.setError(e.getClass().getSimpleName());
            error.setStatus(500);
            throw new ArtechException(error);
        } catch (Throwable e) {

            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro de Sistema: Erro de IO no upload da imagem");
            error.setError(e.getClass().getSimpleName());
            error.setStatus(500);
            throw new ArtechException(error);
        }
    }


    public String getImageExtensionFromBase64(String imageBase64){
        if (!isBase64Image(imageBase64)){
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro negocial: verifique na documetação da API.");
            error.setDetails("A imagem enviada devem ser tratada como base64.");
            error.setStatus(422);
            throw new ArtechException(error);
        }

        String regex = "data:image/([a-zA-Z0-9]+);base64,";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(imageBase64);
        if (matcher.find()) {
            String extensao = matcher.group(1);
            return extensao;
        }
        return null;

    }



    public boolean isBase64Image(String input) {
        Pattern pattern = Pattern.compile(BASE64_IMAGE_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private String extractBase64(String base64Image) {
        String regex = "(?<=^data:image/[a-zA-Z0-9]+;base64,)(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(base64Image);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
