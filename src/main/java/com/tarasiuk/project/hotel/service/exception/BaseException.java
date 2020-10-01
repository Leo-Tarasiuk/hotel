package com.tarasiuk.project.hotel.service.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public abstract class BaseException extends RuntimeException {

    private int errorCode;
    private String title;
    private String message;
    private HttpStatus httpStatus;

    public BaseException(int errorCode, String title, HttpStatus httpStatus) {
        super();
        this.errorCode =  errorCode;
        this.title = title;
        this.httpStatus = httpStatus;
    }

    public BaseException(String message, Throwable cause, int errorCode, String title, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.title = title;
        this.httpStatus = httpStatus;
    }

    public BaseException(String message, String title, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.title = title;
    }

    public BaseException(String message, String title) {
        this.message = message;
        this.title = title;
    }

    public BaseException(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public BaseException(Throwable cause, int errorCode, String title, HttpStatus httpStatus) {
        super(cause);
        this.errorCode = errorCode;
        this.title = title;
        this.httpStatus = httpStatus;
    }

    public BaseException(String message) {
        this.message = message;
    }
}
