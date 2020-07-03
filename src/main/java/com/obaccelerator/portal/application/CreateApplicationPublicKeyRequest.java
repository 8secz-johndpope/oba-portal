package com.obaccelerator.portal.application;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateApplicationPublicKeyRequest {
    @NotEmpty
    private String publicKey;
}
