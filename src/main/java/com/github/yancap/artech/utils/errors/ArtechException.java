package com.github.yancap.artech.utils.errors;

import com.github.yancap.artech.utils.enums.TypeError;
import jakarta.ws.rs.core.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ArtechException extends RuntimeException {


    private String message;
    private String details;
    private String error = "ArtechException";
    private String type = TypeError.ERROR.name();
    private Integer status = 500;

    public ArtechException(){
        this.status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        this.message = "Erro desconhecido do sistema.";
        this.details = "Por favor. consulte o LOG do sistema: TODO.";
        this.error = "Unknown";
    }

    public ArtechException(Throwable exception){

        this.status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        this.message = "Erro de sistema não mapeado.";
        this.details = exception.getMessage();
        this.error = exception.toString();

    }
    public ArtechException(ArtechError error){

        this.status = error.getStatus();
        this.message = error.getMessage();
        this.details = error.getDetails();
        this.error = error.getError();
        this.type = error.getType();
    }


    public ArtechException(int status, String message, String details, TypeError type){
        this.status = status;
        this.message = message;
        this.details = details;
        this.error = error;
        this.type = type.name();
    }

    public ArtechException(int status, String message, String details){
        this.status = status;
        this.message = message;
        this.details = details;
    }


    public ArtechException(int status, String message){
        this.status = status;
        this.message = message;
        this.details = "";
    }

}
