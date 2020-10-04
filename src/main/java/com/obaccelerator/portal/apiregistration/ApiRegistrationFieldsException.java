package com.obaccelerator.portal.apiregistration;

import lombok.Getter;

@Getter
public class ApiRegistrationFieldsException extends RuntimeException {

    private String code;
    private String message;

    public ApiRegistrationFieldsException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

