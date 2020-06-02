package com.obaccelerator.portal.auth.spring;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class PortalPreAuthenticatedAuthenticationManager implements AuthenticationManager {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PreAuthenticatedAuthenticationToken token = (PreAuthenticatedAuthenticationToken) authentication;
        PreAuthenticatedAuthenticationToken innerToken = (PreAuthenticatedAuthenticationToken) token.getPrincipal();
        if (innerToken.isAuthenticated() && innerToken.getAuthorities() != null && innerToken.getAuthorities().size() > 0) {
            return new PreAuthenticatedAuthenticationToken(innerToken, null, innerToken.getAuthorities());
        }

        throw new InsufficientAuthenticationException("Not authenticated");
    }
}
