package com.github.yancap.artech.utils.errors;

import org.hibernate.JDBCException;

import com.github.yancap.artech.utils.enums.TypeError;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalErrorMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        System.out.println(exception.getMessage());
        var artechError = new ArtechError();
        artechError.setError(exception.getClass().getSimpleName());
        artechError.setStatus(500);
        artechError.setType(TypeError.ERROR);
        artechError.setMessage("Ops! Ocorreu um erro no sistema. Tente novamente em instantes.");
        artechError.setDetails("Nossa equipe já está atuando, aguarde um momento.");
        exception.printStackTrace();
        return Response.status(artechError.getStatus())
                .entity(artechError)
                .build();
    }

    
    public Response toResponse(JDBCException exception) {
        System.out.println(exception.getMessage());
        System.out.println(exception.getErrorMessage());
        System.out.println(exception.getSQLState());
        System.out.println(exception.getSQL());
        System.out.println("Implementação 2");
        var artechError = new ArtechError();
        artechError.setError(exception.getClass().getSimpleName());
        artechError.setStatus(500);
        artechError.setType(TypeError.ERROR);
        artechError.setMessage("Ops! Ocorreu um erro em nossa base de dados. Implementação 2");
        artechError.setDetails("Aguarde o sistema voltar a normalidae e tente novamente mais tarde. Implementação 2");
        exception.printStackTrace();
        return Response.status(artechError.getStatus())
                .entity(artechError)
                .build();
    }

    
}
