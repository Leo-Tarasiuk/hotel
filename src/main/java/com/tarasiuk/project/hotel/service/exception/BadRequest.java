package com.tarasiuk.project.hotel.service.exception;

public class BadRequest extends BaseException {

    public BadRequest(String message, int errorCode) {
        super(message, errorCode);
    }
}
