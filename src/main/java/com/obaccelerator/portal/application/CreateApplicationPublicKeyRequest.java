package com.obaccelerator.portal.application;

import javax.validation.constraints.NotEmpty;

public class CreateApplicationPublicKeyRequest {
    @NotEmpty
    private String publicKey;
}
