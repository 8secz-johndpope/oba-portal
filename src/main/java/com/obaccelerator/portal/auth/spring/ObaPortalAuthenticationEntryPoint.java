package com.obaccelerator.portal.auth.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obaccelerator.common.error.ObaError;
import com.obaccelerator.common.error.ObaErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Intercepts the Spring Security's AccessDeniedException so that I can turn it into a client message
 *
 * We have 3 places where exceptions are handled : ObaAuthenticationEntryPoint, ExceptionHandlingFilter
 * and ObaExceptionHandler
 */
@Slf4j
public class ObaPortalAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("AUTHENTICATION EXCEPTION CAUGHT BY ENTRYPOINT", authException);
        ObaErrorMessage message = new ObaErrorMessage(ObaError.OBA_ACCESS_DENIED);
        OutputStream out = response.getOutputStream();
        response.setHeader("content-type", "application/json");
        response.setStatus(403);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, message);
        out.flush();
    }
}
