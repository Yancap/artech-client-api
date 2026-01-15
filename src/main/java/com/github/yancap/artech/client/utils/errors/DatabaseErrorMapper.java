package com.github.yancap.artech.client.utils.errors;

import org.hibernate.JDBCException;

import com.github.yancap.artech.client.utils.enums.TypeError;

import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DatabaseErrorMapper implements ExceptionMapper<JDBCException> {

    @Override
    public Response toResponse(JDBCException exception) {
        System.out.println(exception.getMessage());
        System.out.println(exception.getErrorMessage());
        System.out.println(exception.getSQLState());
        System.out.println(exception.getSQL());
        var artechError = new ArtechError();
        artechError.setError(exception.getClass().getSimpleName());
        artechError.setStatus(500);
        artechError.setType(TypeError.ERROR);
        artechError.setMessage("Ops! Ocorreu um erro em nossa base de dados.");
        artechError.setDetails("Verifique com a equipe de TI e tente novamente mais tarde.");
        return Response.status(artechError.getStatus())
                .entity(artechError)
                .build();
    }

}
