package com.gcoce.bc.ws.payload.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SuccessResponse<T> {
    private String status;
    private int code;
    private String message;
    private T data;

    public SuccessResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus.name();
        this.code = httpStatus.value();
        this.message = message;
    }

    public SuccessResponse(HttpStatus httpStatus, String message, T data) {
        this.status = httpStatus.name();
        this.code = httpStatus.value();
        this.message = message;
        this.data = data;
    }
}
