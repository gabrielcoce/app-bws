package com.gcoce.bc.ws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Gabriel Coc Estrada
 * @since 7/06/2023
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HcException extends RuntimeException {
    public HcException(String message) {
        super(message);
    }
}
