package com.obaccelerator.portal.authentication.spring;

import com.obaccelerator.portal.authentication.cognito.CognitoService;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.session.FirstTimeSessionService;
import com.obaccelerator.portal.shared.session.SessionService;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.obaccelerator.portal.ObaPortalApplication.ORGANIZATION_ADMIN;

/**
 * This filter firs only for the 'defaultFilterProcessesUrl'. This means it is used whenever the front-end attempts
 * to exchange a Cognito token for a portal session cookie.
 */
@Slf4j
public class ObaCognitoAuthFilter extends AbstractAuthenticationProcessingFilter {

    private CognitoService cognitoService;
    private PortalUserService portalUserService;
    private FirstTimeSessionService firstTimeSessionService;
    private SessionService sessionService;

    public ObaCognitoAuthFilter(String defaultFilterProcessesUrl, CognitoService cognitoService, PortalUserService portalUserService, FirstTimeSessionService firstTimeSessionService, SessionService sessionService) {
        super(defaultFilterProcessesUrl);
        this.cognitoService = cognitoService;
        this.portalUserService = portalUserService;
        this.firstTimeSessionService = firstTimeSessionService;
        this.sessionService = sessionService;
    }

    /**
     * This method fires whenever the /session/ URL is hit. It attempts to validate the Cognito token, it creates
     * an Authentication that Spring can pick up, and if a portal user doesn't exist yet with the provided Cognito
     * ID it is created.
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Validate the token and hget its claims
        Map<String, Object> tokenClaims = cognitoService.verifyAndGetCognitoClaimsFromRequest(request);
        String userEmail = (String) tokenClaims.get("email");

        // Create a simple Principal
        Principal p = new ObaPortalPrincipal(userEmail);

        // Authority is always ORGANIZATION_ADMIN - once we get a more complicated model we can get it from the db for each user
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(ORGANIZATION_ADMIN);

        // Create the token. Credentials aren't needed
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(p, null, Collections.singletonList(grantedAuthority));

        // Find an existing users or create it and set it on the token as 'details'
        Optional<PortalUser> portalUserOptional = portalUserService.findByCognitoId((String) tokenClaims.get("sub"));
        PortalUser portalUser = portalUserOptional.orElseGet(() -> firstTimeSessionService.promoteRegistrationToOrganizationWithUser(tokenClaims));
        token.setDetails(portalUser);

        return token;
    }



    @Value
    @AllArgsConstructor
    private static class ObaPortalPrincipal implements Principal {
        private String name;
    }



}
