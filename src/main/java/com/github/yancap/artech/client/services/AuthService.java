package com.github.yancap.artech.client.services;

import com.github.yancap.artech.client.utils.enums.TypeError;
import com.github.yancap.artech.client.utils.errors.ArtechError;
import com.github.yancap.artech.client.utils.errors.ArtechException;
import com.github.yancap.artech.client.persistence.dto.auth.LoginUserRequestDTO;
import com.github.yancap.artech.client.persistence.dto.auth.UserResponseDTO;
import com.github.yancap.artech.client.persistence.models.Management;
import com.github.yancap.artech.client.persistence.models.User;
import com.github.yancap.artech.client.persistence.repositories.UserRepository;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;


    public User login(LoginUserRequestDTO dto){

        User currentManager = userRepository.findByEmail(dto.email());
        if(currentManager == null){
            throw new ArtechException(401, "Credenciais Inválidas.");
        }

        var isPasswordRight = comparePasswordHash(dto.password(), currentManager.getPassword());

        if(!isPasswordRight) {
            throw new ArtechException(401, "Credenciais Inválidas.");
        }
        return currentManager;


    }

    public UserResponseDTO validateToken(SecurityContext ctx, JsonWebToken jwt){
        if(ctx.getUserPrincipal() == null) {
            throw new ArtechException(401, "Token inválido.");
        }
        if(!(ctx.getUserPrincipal().getName().equals(jwt.getName()))) {
            throw new ArtechException(401, "Token inválido.");
        }
        if (
                jwt.getClaim("email") == null ||
                jwt.getClaim("name") == null
        ) {
            throw new ArtechException(401, "Token inválido.");
        }
        return new UserResponseDTO(jwt);
    }

    public String generateToken(User currentManager){
        return Jwt.issuer("ARTECH")
            .upn("quarkus-artech.cms.io")
            .subject(currentManager.getEmail())
            .audience("CLIENT")
            .groups(
                    new HashSet<>(
                            Collections.singletonList("User")
                    )
            )
            .claim("name", currentManager.getName())
            .claim("id", currentManager.getId())
            .claim("email", currentManager.getEmail())
            .claim("urlAvatar", currentManager.getUrlAvatar())
            .expiresIn(60 * 60 * 24)
            .sign();
    }

    public String generateHashPassword(String password) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = algorithm.digest(password.getBytes("UTF-8"));

            StringBuilder hexStringPassword = new StringBuilder();
            for (byte b : messageDigest) {
                hexStringPassword.append(String.format("%02X", 0xFF & b));
            }
            return hexStringPassword.toString();
        } catch (UnsupportedEncodingException e) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro do Sistema: O enconding escolhido não suporta a cadeia de caracteres");
            error.setDetails("Verifique a implementação no serviço");
            error.setError(e.getClass().getSimpleName());
            error.setStatus(500);
            throw new ArtechException(error);
        } catch (NoSuchAlgorithmException e) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro do Sistema: Não foi suportada a implementação do algoritmo de hash");
            error.setDetails("Verifique a implementação no serviço");
            error.setError(e.getClass().getSimpleName());
            error.setStatus(500);
            throw new ArtechException(error);
        } catch (Throwable e) {
            throw new ArtechException(e);
        }

    }

    public boolean comparePasswordHash(String nonHashPassword, String hashPassword) {
        try {
            if (nonHashPassword.equals(hashPassword)) return true;
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = algorithm.digest(nonHashPassword.getBytes("UTF-8"));

            StringBuilder hexStringPassword = new StringBuilder();
            for (byte b : messageDigest) {
                hexStringPassword.append(String.format("%02X", 0xFF & b));
            }
            return hexStringPassword.toString().equals(hashPassword);
        } catch (UnsupportedEncodingException e) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro do Sistema: O enconding escolhido não suporta a cadeia de caracteres");
            error.setDetails("Verifique a implementação no serviço");
            error.setError(e.getClass().getSimpleName());
            error.setStatus(500);
            throw new ArtechException(error);
        } catch (NoSuchAlgorithmException e) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Erro do Sistema: Não foi suportada a implementação do algoritmo de hash");
            error.setDetails("Verifique a implementação no serviço");
            error.setError(e.getClass().getSimpleName());
            error.setStatus(500);
            throw new ArtechException(error);
        } catch (Throwable e) {
            throw new ArtechException(e);
        }
    }
}
