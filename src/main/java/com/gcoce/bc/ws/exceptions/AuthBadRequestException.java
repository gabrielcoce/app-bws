package com.gcoce.bc.ws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthBadRequestException extends RuntimeException {
    public AuthBadRequestException(String exception) { super(exception);}
}
