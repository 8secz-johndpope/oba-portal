package com.obaccelerator.portal;

import com.obaccelerator.common.error.ObaBaseExceptionHandler;
import com.obaccelerator.common.error.ObaError;
import com.obaccelerator.common.error.ObaErrorMessage;
import com.obaccelerator.portal.registration.RegistrationAlreadyExistsException;
import com.obaccelerator.portal.session.InvalidCognitoTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ObaPortalExceptionHandler extends ObaBaseExceptionHandler {


    @ExceptionHandler(value = RegistrationAlreadyExistsException.class)
    public ResponseEntity<ObaErrorMessage> handleRegistrationAlreadyExistsException(RegistrationAlreadyExistsException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.PORTAL_REGISTRATION_ALREADY_EXISTS);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = InvalidCognitoTokenException.class)
    public ResponseEntity<ObaErrorMessage> handleInvalidCognitoTokenException(InvalidCognitoTokenException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(400, null, "Your message contained errors");
        errorMessage.addFieldErrors(collectBindingErrors(e));
        return handle(errorMessage, e);
    }

}
