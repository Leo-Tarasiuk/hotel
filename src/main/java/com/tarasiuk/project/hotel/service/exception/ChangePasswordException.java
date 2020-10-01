package com.tarasiuk.project.hotel.service.exception;

public class ChangePasswordException extends BaseException {
    public ChangePasswordException(String message, int errorCode) {
        super(message, errorCode);
    }
}
