package com.gcoce.bc.ws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Gabriel Coc Estrada
 * @since 11/06/2023
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AgricultorException extends RuntimeException {
    public AgricultorException(String message){ super(message);}
}
