package com.gcoce.bc.ws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Gabriel Coc Estrada
 * @since 20/05/2023
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class BeneficioException extends RuntimeException {
    public BeneficioException(String message) {
        super(message);
    }
}
