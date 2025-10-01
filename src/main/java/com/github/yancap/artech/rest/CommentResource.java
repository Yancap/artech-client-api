package com.github.yancap.artech.rest;

import com.github.yancap.artech.persistence.dto.comments.CreateCommentRequestDTO;
import com.github.yancap.artech.services.AuthService;
import com.github.yancap.artech.services.CommentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("api/v1/comment/")
@RolesAllowed({ "User" })
@Transactional
public class CommentResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    AuthService authService;

    @Inject
    CommentService commentService;

    @POST
    public Response create(CreateCommentRequestDTO dto, @Context SecurityContext ctx){
        var user = authService.validateToken(ctx, jwt);
        commentService.create(dto, user.email());
        return Response.status(201).build();
    }

    @DELETE
    @Path("{commentId}")
    public Response deleteComment(
            @PathParam("commentId") Long commentId,
            @Context SecurityContext ctx
    ) {
        var user = authService.validateToken(ctx, jwt);
        commentService.deleteById(commentId, user.email());
        return Response.noContent().build();
    }
}
