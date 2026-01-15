package com.github.yancap.artech.client.rest;

import com.github.yancap.artech.client.services.AuthService;
import com.github.yancap.artech.client.persistence.dto.auth.LoginUserRequestDTO;
import com.github.yancap.artech.client.persistence.dto.auth.TokenResponseDTO;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("client/api/v1/auth/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @Inject
    JsonWebToken jwt;


    @POST
    @Path("login/authentication")
    @PermitAll
    public Response authentication(LoginUserRequestDTO dto) {
        var currentManager = authService.login(dto);
        var token = authService.generateToken(currentManager);
        return Response.status(200).entity(new TokenResponseDTO(token)).build();
    }

    @GET
    @Path("login/access")
    @RolesAllowed({ "User" })
    public Response access(@Context SecurityContext ctx) {
        var responseDTO = authService.validateToken(ctx, jwt);
        return Response.status(200).entity(responseDTO).build();
    }

}
