package com.obaccelerator.portal.authentication.spring;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;


public class ObaCognitoPreAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }

}
