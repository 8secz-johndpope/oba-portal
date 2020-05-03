package com.obaccelerator.portal.session;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Collections;

import static com.obaccelerator.portal.ObaPortalApplication.ORGANIZATION_ADMIN;

public class SecurityContextHelper {

    public static void portalUserToSecurityContext(PortalUser portalUser) {
        // Authority is always ORGANIZATION_ADMIN - once we get a more complicated model we can get it from the db for each user
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(ORGANIZATION_ADMIN);
        // Create a simple Principal
        Principal p = new ObaPortalPrincipal(portalUser.getId().toString());
        // Create the token. Credentials aren't needed
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(p, null, Collections.singletonList(grantedAuthority));
        token.setDetails(portalUser);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public static Authentication getPreAuthenticatedPrincipal(PortalUser portalUser) {
        // Authority is always ORGANIZATION_ADMIN - once we get a more complicated model we can get it from the db for each user
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(ORGANIZATION_ADMIN);
        // Create a simple Principal
        Principal p = new ObaPortalPrincipal(portalUser.getId().toString());
        // Create the token. Credentials aren't needed
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(p, null, Collections.singletonList(grantedAuthority));
        token.setDetails(portalUser);
        return token;
    }

}
