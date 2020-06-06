package com.obaccelerator.portal.redirecturl;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateRedirectUrlRequest {
    @NotEmpty
    private String redirectUrl;
}
