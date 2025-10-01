package com.github.yancap.artech.rest;

import com.github.yancap.artech.persistence.dto.user.ChangeAvatarRequestDTO;
import com.github.yancap.artech.persistence.dto.user.ChangePasswordRequestDTO;
import com.github.yancap.artech.persistence.dto.user.UserRegisterDTO;
import com.github.yancap.artech.services.AuthService;
import com.github.yancap.artech.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.jwt.JsonWebToken;

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

        String currentServerURL = protocol + "://" + serverName + ":" + serverPort;
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

        String currentServerURL = protocol + "://" + serverName + ":" + serverPort;
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
