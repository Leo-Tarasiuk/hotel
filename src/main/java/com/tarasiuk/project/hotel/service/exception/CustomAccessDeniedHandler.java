package com.tarasiuk.project.hotel.service.exception;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOG
            = Logger.getLogger(CustomAccessDeniedHandler.class);

    private final CustomLocaleResolver customLocaleResolver;

    public CustomAccessDeniedHandler(CustomLocaleResolver customLocaleResolver) {
        this.customLocaleResolver = customLocaleResolver;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOG.warn("User: " + auth.getName()
                    + " attempted to access the protected URL: "
                    + request.getRequestURI());
            response.setContentType("error");
            response.getOutputStream().print("{\"error\":403,");
            response.getOutputStream().print("\"title\":\"" + customLocaleResolver.messageSource().getMessage
                    ("errorTitle.AccessDeniedException", null, LocaleContextHolder.getLocale()) + "\",");
            response.getOutputStream().print("\"message\":\"" + customLocaleResolver.messageSource().getMessage
                        ("errorMessage.AccessDeniedException", new String[]{auth.getName()}, LocaleContextHolder.getLocale()) + "\"}");

        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
