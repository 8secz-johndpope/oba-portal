package com.obaccelerator.portal;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.common.error.ObaBaseExceptionHandler;
import com.obaccelerator.common.error.ObaError;
import com.obaccelerator.common.error.ObaErrorMessage;
import com.obaccelerator.portal.session.InvalidCognitoTokenException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ObaPortalExceptionHandler extends ObaBaseExceptionHandler {

    @ExceptionHandler(value = {DataAccessException.class})
    public ResponseEntity<ObaErrorMessage> handleDataAccessException(DataAccessException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_DB_EXCEPTION);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {SQLException.class})
    public ResponseEntity<ObaErrorMessage> handleDataAccessException(SQLException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_DB_EXCEPTION);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {HttpMessageConversionException.class})
    public ResponseEntity<ObaErrorMessage> handleHttpMessageNotReadableException(HttpMessageConversionException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_CLIENT_ERROR_INVALID_REQUEST);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {ServletRequestBindingException.class})
    public ResponseEntity<ObaErrorMessage> handleServletRequestBindingException(ServletRequestBindingException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_CLIENT_ERROR_INVALID_REQUEST);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ObaErrorMessage> handleEntityNotFoundException(EntityNotFoundException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_ENTITY_NOT_FOUND);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ObaErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(400, null, "Your message contained errors");
        errorMessage.addFieldErrors(collectBindingErrors(e));
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = InvalidCognitoTokenException.class)
    public ResponseEntity<ObaErrorMessage> handleInvalidCognitoTokenException(InvalidCognitoTokenException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(400, null, "Your message contained errors");
        errorMessage.addFieldErrors(collectBindingErrors(e));
        return handle(errorMessage, e);
    }

    private Map<String, String> collectBindingErrors(Throwable error) {
        Map<String, String> bindingErrors;
        if (error instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) error).getBindingResult();
            bindingErrors = new HashMap<>();

            bindingResult.getAllErrors().forEach(e -> {
                //SpringValidatorAdapter adapter = (SpringValidatorAdapter) e;
                FieldError fieldError = (FieldError) e;
                String field = fieldError.getField();
                String defaultMessage = e.getDefaultMessage();
                bindingErrors.put(field, defaultMessage);
            });
            return bindingErrors;
        }

        return null;
    }
}
