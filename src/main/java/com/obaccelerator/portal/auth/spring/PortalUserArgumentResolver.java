package com.obaccelerator.portal.auth.spring;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

@Component
public class PortalUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PortalUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Principal userPrincipal = webRequest.getUserPrincipal();
        PreAuthenticatedAuthenticationToken preAuthToken = (PreAuthenticatedAuthenticationToken) userPrincipal;
        UsernamePasswordAuthenticationToken upToken = (UsernamePasswordAuthenticationToken) preAuthToken.getPrincipal();
        return upToken.getDetails();
    }
}