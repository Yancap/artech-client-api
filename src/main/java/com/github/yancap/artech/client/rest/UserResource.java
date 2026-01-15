package com.github.yancap.artech.client.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.github.yancap.artech.client.services.AuthService;
import com.github.yancap.artech.client.services.UserService;
import com.github.yancap.artech.client.persistence.dto.user.ChangeAvatarRequestDTO;
import com.github.yancap.artech.client.persistence.dto.user.ChangePasswordRequestDTO;
import com.github.yancap.artech.client.persistence.dto.user.UserRegisterDTO;

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

@Path("client/api/v1/user/")
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
       
        userService.register(dto);
        return Response.status(201).build();
    }


    @PUT
    @Path("change/avatar")
    @RolesAllowed({ "User" })
    public Response changeAvatar(ChangeAvatarRequestDTO dto, @Context SecurityContext ctx) {
        var user = authService.validateToken(ctx, jwt);
        
        userService.changeAvatar(dto, user.email());
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
