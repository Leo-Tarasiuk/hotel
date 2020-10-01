package com.tarasiuk.project.hotel.service.exception;

public class InvalidLoginException extends BaseException {

    public InvalidLoginException(String message, int errorCodes) {
        super(message, errorCodes);
    }

}
