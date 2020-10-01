package com.tarasiuk.project.hotel.service.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes {
    DELETE_EXCEPTION("NO_CONTENT", 204),
    SAVE_EXCEPTION("SAVE_EXCEPTION", 406),
    ACCESS_DENIED_EXCEPTION("ACCESS_DENIED_EXCEPTION", 403),
    NOT_FOUND("NOT_FOUND", 404),
    NOT_FOUND_DEPENDENCY("NO_SUCH_DEPENDENCY_FOUND", 404),
    INVALID_LOGIN_EXCEPTION("INVALID_LOGIN_EXCEPTION", 404),
    INTERNAL_SERVER_ERROR("UNAUTHORIZED", 500),
    LESSON_EXPIRED("LESSON HAS EXPIRED", 450),
    OFFER_ALREADY_EXISTS("OFFER ALREADY EXISTS", 471),
    LESSON_CLIENT_NOT_REGISTERED("CLIENT IS NOT REGISTERED FOR THIS LESSON", 451),
    LESSON_CLIENT_IS_REGISTERED("CLIENT IS ALREADY REGISTERED FOR THIS LESSON", 452),
    LESSON_NO_PLACES_LEFT("NO PLACES LEFT FOR THIS LESSON", 453),
    REVIEW_INVALID_RATING("INVALID RATING VALUE", 461),
    REVIEW_NO_PARAMETERS("NO ORGANIZATION ID OR COACH ID WAS FOUND", 462),
    REVIEW_BOTH_PARAMETERS("CANNOT ADD REVIEW TO ORGANIZATION AND COACH AT THE SAME TIME", 463),
    USER_IS_PRESENT("USER WITH PROVIDED PROPERTIES IS ALREADY PRESENT", 411),
    BAD_REQUEST("REQUEST ARGUMENTS IS NOT VALID",400),
    CHANGE_PASSWORD("PASSWORD MISMATCH", 400);

    private String title;
    private int errorCode;

    ErrorCodes(String title, int errorCode) {
        this.title = title;
        this.errorCode = errorCode;
    }
}
