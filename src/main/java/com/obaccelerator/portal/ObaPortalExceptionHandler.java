package com.obaccelerator.portal;

import com.obaccelerator.common.error.ObaBaseExceptionHandler;
import com.obaccelerator.common.error.ObaError;
import com.obaccelerator.common.error.ObaErrorMessage;
import com.obaccelerator.portal.auth.NotAuthorizedException;
import com.obaccelerator.portal.registration.RegistrationAlreadyExistsException;
import com.obaccelerator.portal.session.InvalidCognitoTokenException;
import com.obaccelerator.portal.shared.session.NoSessionException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ObaPortalExceptionHandler extends ObaBaseExceptionHandler {


    @ExceptionHandler(value = RegistrationAlreadyExistsException.class)
    public ResponseEntity<ObaErrorMessage> handleRegistrationAlreadyExistsException(RegistrationAlreadyExistsException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.PORTAL_REGISTRATION_ALREADY_EXISTS);
        return handleAsError(errorMessage, e);
    }

    @ExceptionHandler(value = InvalidCognitoTokenException.class)
    public ResponseEntity<ObaErrorMessage> handleInvalidCognitoTokenException(InvalidCognitoTokenException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(400, null, "Your message contained errors");
        errorMessage.addFieldErrors(collectBindingErrors(e));
        return handleAsError(errorMessage, e);
    }

    @ExceptionHandler(value = NoSessionException.class)
    public ResponseEntity<ObaErrorMessage> handleMissingSessionException(NoSessionException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.PORTAL_MISSING_SESSION);
        return handleAsInfo(errorMessage, e);
    }

    @ExceptionHandler(value = NotAuthorizedException.class)
    public ResponseEntity<ObaErrorMessage> handleMissingSessionException(NotAuthorizedException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.PORTAL_NOT_AUTHORIZED);
        return handleAsError(errorMessage, e);
    }

    /**
     * Combining a custom http configuration in a Spring configuration that extends WebSecurityConfigurerAdapter with
     * the use of @PreAuthorize annotations results in AccessDeniedException being caught and translated to a 500.
     * I stopped trying to figure out why exactly and decided to handle it is a regular application error.
     *
     * Setting the authorization rules with antMatcher rules in the http configuration will translate the error
     * correctly to a 403 response, but it will trigger Spring to redirect to an HTML error page, so didn't go
     * for that solution.
     *
     * See https://stackoverflow.com/questions/43554489/spring-mvc-accessdeniedexception-500-error-received-instead-of-custom-401-error
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ObaErrorMessage> handleAccessDeniedException(AccessDeniedException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_ACCESS_DENIED);
        return handleAsError(errorMessage, e);
    }

}
