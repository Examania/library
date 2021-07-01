package com.examania.library.httpclient;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExHttpException extends RuntimeException {
    HttpStatus status;
    public ExHttpException(String message, HttpStatus status){
        super(message);
        this.status=status;
    }
}
