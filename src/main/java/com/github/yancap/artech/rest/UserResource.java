package com.github.yancap.artech.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.github.yancap.artech.persistence.dto.user.ChangeAvatarRequestDTO;
import com.github.yancap.artech.persistence.dto.user.ChangePasswordRequestDTO;
import com.github.yancap.artech.persistence.dto.user.UserRegisterDTO;
import com.github.yancap.artech.services.AuthService;
import com.github.yancap.artech.services.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

@Path("api/v1/user/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @Context
    UriInfo uriInfo;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("register")
    public Response register(UserRegisterDTO dto) {
        String protocol = uriInfo.getRequestUri().getScheme(); // "http" ou "https"
        String serverName = uriInfo.getRequestUri().getHost(); // Nome do servidor (localhost, IP ou domínio)
        int serverPort = uriInfo.getRequestUri().getPort(); // Número da porta

        String currentServerURL;
        if (serverPort > 0) {
            currentServerURL = protocol + "://" + serverName + ":" + serverPort;  
        } else {
            currentServerURL = protocol + "://" + serverName + "/cms";  
        }
        userService.register(dto, currentServerURL);
        return Response.status(201).build();
    }


    @PUT
    @Path("change/avatar")
    @RolesAllowed({ "User" })
    public Response changeAvatar(ChangeAvatarRequestDTO dto, @Context SecurityContext ctx) {
        var user = authService.validateToken(ctx, jwt);
        String protocol = uriInfo.getRequestUri().getScheme(); // "http" ou "https"
        String serverName = uriInfo.getRequestUri().getHost(); // Nome do servidor (localhost, IP ou domínio)
        int serverPort = uriInfo.getRequestUri().getPort(); // Número da porta

        String currentServerURL;
        if (serverPort > 0) {
            currentServerURL = protocol + "://" + serverName + ":" + serverPort;  
        } else {
            currentServerURL = protocol + "://" + serverName + "/cms";  
        }
        userService.changeAvatar(dto, user.email(), currentServerURL);
        return Response.status(204).build();
    }



    @PUT
    @Path("change/password")
    @RolesAllowed({ "User" })
    public Response changePassword(ChangePasswordRequestDTO dto, @Context SecurityContext ctx) {
        var user = authService.validateToken(ctx, jwt);
        userService.changePassword(dto, user.email());
        return Response.status(204).build();
    }


}
