package com.github.yancap.artech.services;

import com.github.yancap.artech.persistence.dto.user.ChangeAvatarRequestDTO;
import com.github.yancap.artech.persistence.dto.user.ChangePasswordRequestDTO;
import com.github.yancap.artech.persistence.dto.user.UserRegisterDTO;
import com.github.yancap.artech.persistence.models.User;
import com.github.yancap.artech.persistence.repositories.UserRepository;
import com.github.yancap.artech.utils.enums.TypeError;
import com.github.yancap.artech.utils.errors.ArtechError;
import com.github.yancap.artech.utils.errors.ArtechException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;


    @Inject
    private ImageStorageService imageStorageService;


    @Inject
    private AuthService authService;

    public void register(UserRegisterDTO dto, String currentServerURL){
        var user = userRepository.findByEmail(dto.email());
        if (user != null) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Já existe um usuário com esse e-mail.");
            error.setStatus(404);
            throw new ArtechException(error);
        }
        String urlAvatar = "";
        if (!(dto.imageBlob() == null || dto.imageBlob().isBlank())) {
            urlAvatar =
                currentServerURL + imageStorageService.uploadImage(dto.imageBlob(), "images/users/");
        }
        var userCreated = new User();
        var hashPassword = authService.generateHashPassword(dto.password());

        userCreated.setName(dto.name());
        userCreated.setEmail(dto.email());
        userCreated.setPassword(hashPassword);
        userCreated.setUrlAvatar(urlAvatar);
        userRepository.persist(userCreated);

    }


    public void changePassword(ChangePasswordRequestDTO dto, String email){
        var user = userRepository.findByEmail(email);
        if (user == null) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Usuário não encontrado.");
            error.setDetails("Verifique se o usuário existe ou se foi excluído.");
            error.setStatus(404);
            throw new ArtechException(error);
        }

        var hashPassword = authService.generateHashPassword(dto.newPassword());

        user.setPassword(hashPassword);
        userRepository.persist(user);

    }

    public void changeAvatar(ChangeAvatarRequestDTO dto, String email, String currentServerURL){
        var user = userRepository.findByEmail(email);
        if (user == null) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Usuário não encontrado.");
            error.setDetails("Verifique se o usuário existe ou se foi excluído.");
            error.setStatus(404);
            throw new ArtechException(error);
        }

        String urlAvatar =
                currentServerURL + imageStorageService.uploadImage(dto.imageBlob(), "images/users/");

        user.setUrlAvatar(urlAvatar);
        userRepository.persist(user);

    }

}
