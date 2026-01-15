package com.github.yancap.artech.client.utils.errors;

import com.github.yancap.artech.client.utils.enums.TypeError;
import jakarta.ws.rs.core.Response;
import lombok.Data;

@Data
public class ArtechError {

    private String details;
    private String message;
    private String error;
    private String privateMessageError;
    private TypeError type = TypeError.ERROR;
    private Integer status = 500;

    public ArtechError(RuntimeException exception){
        exception.printStackTrace();
        this.status = 500;
        this.message = "Erro de sistema não mapeado.";
        this.details = exception.getMessage();
        this.error = exception.getClass().getSimpleName();
        this.type = TypeError.ERROR;
    }

    public ArtechError(ArtechException exception){
        exception.printStackTrace();
        this.status = exception.getStatus();
        this.details = exception.getDetails();
        this.message = exception.getMessage();
        this.error = exception.getError();
        this.type = TypeError.valueOf(exception.getType());
    }


    public ArtechError(){
        this.status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        this.message = "Erro desconhecido do sistema.";
        this.details = "";
        this.error = "Unknown";
    }

    public String getType() {
        return type.name();
    }
}
