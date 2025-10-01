package com.github.yancap.artech.utils.errors;

import com.github.yancap.artech.utils.enums.TypeError;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalErrorMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        var artechError = new ArtechError();
        artechError.setError(exception.getClass().getSimpleName());
        artechError.setStatus(500);
        artechError.setType(TypeError.ERROR);
        artechError.setMessage("Erro de Sistema: olhe os Logs do serviço");
        artechError.setDetails(exception.getMessage());
        exception.printStackTrace();
        return Response.status(artechError.getStatus())
                .entity(artechError)
                .build();
    }

}
