package com.gcoce.bc.ws.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class ErrorResponse {
    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Guatemala")
    private Date timestamp;
    private int status;
    //General error message about nature of error
    private String error;

    private String message;

    private String path;
    private List<String> details;


    public ErrorResponse() {
        timestamp = new Date();
    }

    public ErrorResponse(HttpStatus httpStatus, String message, List<String> details) {
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(HttpStatus httpStatus, String message, List<String> details, String path) {
        this(httpStatus, message, details);
        this.path = path;
    }

    public ErrorResponse(HttpStatus httpStatus, String message, String path) {
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
        this.path = path;
    }
    public ErrorResponse(HttpStatus httpStatus, String message){
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
    }
}
