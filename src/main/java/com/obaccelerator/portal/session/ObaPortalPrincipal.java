package com.obaccelerator.portal.session;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.security.Principal;

@Value
@AllArgsConstructor
public class ObaPortalPrincipal implements Principal {
    private String name;
}
