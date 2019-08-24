package com.obaccelerator.portal.session;

public class InvalidCognitoTokenException extends RuntimeException {

    public InvalidCognitoTokenException(Throwable throwable) {
        super(throwable);
    }

    public InvalidCognitoTokenException() {
    }
}
