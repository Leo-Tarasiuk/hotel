package com.tarasiuk.project.hotel.service.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ErrorResponse {

    @JsonIgnore
    @Autowired
    private CustomLocaleResolver customLocaleResolver;

    @JsonIgnore
    private Locale locale;

    private int errorCode;
//    private ErrorCodes Code;
    private String title;
    private String message;
    private int httpStatus;
    private Map<String, String> getExtra;

    public String getTitle() {
        LocaleContextHolder.getLocale();
        return title;
    }

    public void setTitle(String title, Locale locale) {
        this.title = title;
        this.locale = locale;
    }

    public void setMessage(String message) {
        this.message = message;
        LocaleContextHolder.getLocale();
    }
}
