package com.obaccelerator.portal.auth;

import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.session.ObaPortalPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.security.Principal;

public class PrincipalUtil {

    public static ObaPortalPrincipal portalPrincipalFromPrincipal(Principal principal) {
        PreAuthenticatedAuthenticationToken preAuthToken = (PreAuthenticatedAuthenticationToken) principal;
        UsernamePasswordAuthenticationToken upToken = (UsernamePasswordAuthenticationToken) preAuthToken.getPrincipal();
        return (ObaPortalPrincipal) upToken.getPrincipal();
    }

    public static PortalUser portalUserFromPrincipal(Principal principal) {
        PreAuthenticatedAuthenticationToken preAuthToken = (PreAuthenticatedAuthenticationToken) principal;
        UsernamePasswordAuthenticationToken upToken = (UsernamePasswordAuthenticationToken) preAuthToken.getPrincipal();
        return (PortalUser) upToken.getDetails();
    }
}
