package com.tarasiuk.project.hotel.service.exception;


import lombok.Getter;


@Getter
public class DataNotFoundException extends BaseException implements ExceptionExtraKey {

    private Long id;
    private String field;

    public DataNotFoundException(String message, int errorCode, Long id) {
        super(message, errorCode);
        this.id = id;
    }

    public DataNotFoundException(String message, int errorCode, String field) {
        super(message, errorCode);
        this.field = field;
    }
}
