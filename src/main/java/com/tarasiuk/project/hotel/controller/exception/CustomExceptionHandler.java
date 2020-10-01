package com.tarasiuk.project.hotel.controller.exception;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import com.tarasiuk.project.hotel.model.Account;
import com.tarasiuk.project.hotel.service.AccountService;
import com.tarasiuk.project.hotel.service.exception.BadRequest;
import com.tarasiuk.project.hotel.service.exception.BaseException;
import com.tarasiuk.project.hotel.service.exception.BookingException;
import com.tarasiuk.project.hotel.service.exception.ChangePasswordException;
import com.tarasiuk.project.hotel.service.exception.DataNotFoundException;
import com.tarasiuk.project.hotel.service.exception.DeleteException;
import com.tarasiuk.project.hotel.service.exception.ErrorCodes;
import com.tarasiuk.project.hotel.service.exception.InvalidLoginException;
import com.tarasiuk.project.hotel.service.exception.SaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    private final CustomLocaleResolver customLocaleResolver;
    private final AccountService accountService;

    private static final Map<Class<?>, HttpStatus> EXCEPTION_HTTP_STATUS = new HashMap<>();
    private static final Map<Class<?>, String> EXCEPTION_TITLE = new HashMap<>();
    private static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;

    static {
        EXCEPTION_HTTP_STATUS.put(DataNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_HTTP_STATUS.put(InvalidLoginException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_HTTP_STATUS.put(SaveException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_HTTP_STATUS.put(DeleteException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_HTTP_STATUS.put(BookingException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_HTTP_STATUS.put(ChangePasswordException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_HTTP_STATUS.put(BadRequest.class, HttpStatus.BAD_REQUEST);
    }

    static {
        EXCEPTION_TITLE.put(DataNotFoundException.class, "errorTitle.DataNotFoundException");
        EXCEPTION_TITLE.put(InvalidLoginException.class, "errorTitle.InvalidLoginException");
        EXCEPTION_TITLE.put(DeleteException.class, "errorTitle.DeleteException");
        EXCEPTION_TITLE.put(SaveException.class, "errorTitle.SaveException");
        EXCEPTION_TITLE.put(BookingException.class, "errorTitle.BookingException");
        EXCEPTION_TITLE.put(ChangePasswordException.class, "errorTitle.ChangePasswordException");
        EXCEPTION_TITLE.put(BadRequest.class, "errorTitle.BadRequest");
    }

    public CustomExceptionHandler(CustomLocaleResolver customLocaleResolver, AccountService accountService) {
        this.customLocaleResolver = customLocaleResolver;
        this.accountService = accountService;
    }

    @ModelAttribute
    public void authenticated(Authentication authentication, Model model) {
        if (authentication != null) {
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            if (principal != null) {
                Account authenticated = accountService.findByUsername(principal.getUsername());
                model.addAttribute("account", authenticated);
            }
        }
    }

    @ExceptionHandler(value = {BaseException.class})
    private String processExceptionHandler(BaseException ex, Model model) {
        HttpStatus httpStatus = getStatusForException(ex);
        String title = getTitleException(ex);
        model.addAttribute("errorTitle", customLocaleResolver.messageSource().getMessage
                (title, null, LocaleContextHolder.getLocale()));
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode());
        model.addAttribute("httpStatus", httpStatus.value());
        return "error";
    }

    @ExceptionHandler({RuntimeException.class})
    private String runtimeExceptionHandler(RuntimeException ex, Model model) {
        model.addAttribute("errorTitle", customLocaleResolver.messageSource().getMessage
                ("errorTitle.InternalServerError", null, LocaleContextHolder.getLocale()));
        model.addAttribute("errorMessage", customLocaleResolver.messageSource().getMessage
                ("errorMessage.InternalServerError", null, LocaleContextHolder.getLocale()));
        model.addAttribute("errorCode", ErrorCodes.INTERNAL_SERVER_ERROR.getErrorCode());
        model.addAttribute("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error";
    }

    private static HttpStatus getStatusForException(BaseException ex) {
        return EXCEPTION_HTTP_STATUS.get(ex.getClass());
    }

    private static String getTitleException(BaseException ex) {
        return EXCEPTION_TITLE.get(ex.getClass());
    }
}
