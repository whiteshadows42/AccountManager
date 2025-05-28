package br.com.khadijeelzein.accountmanager.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
public class ErrorResponse {
    private HttpStatus status;
    @JsonProperty("mensagem")
    private String message;
    @JsonProperty("erro")
    private String error;

    public ErrorResponse(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String error) {
        super();
        this.status = status;
        this.error = error;
    }
}
