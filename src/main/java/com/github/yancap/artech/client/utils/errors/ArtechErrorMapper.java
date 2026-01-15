package com.github.yancap.artech.client.utils.errors;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ArtechErrorMapper implements ExceptionMapper<ArtechException> {

    @Override
    public Response toResponse(ArtechException exception) {
        var artechError = new ArtechError(exception);
        return Response.status(artechError.getStatus())
                .entity(artechError)
                .build();
    }

}
