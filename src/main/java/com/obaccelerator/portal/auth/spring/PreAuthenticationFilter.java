package com.obaccelerator.portal.auth.spring;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.session.SecurityContextHelper;
import com.obaccelerator.portal.session.Session;
import com.obaccelerator.portal.shared.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.obaccelerator.portal.ObaPortalApplication.SESSION_COOKIE_NAME;

/**
 * This filter authenticates each request by taking the session id from the cookie if it exists and returning a
 * UsernamePasswordAuthenticationToken to Spring Security. Spring internally turns this into a
 * PreAuthenticatedAuthenticationToken. This token later provided to the (required)
 * DummyPreauthenticatedAuthenticationManager, which then provides it to the (required)
 * DummyPreAuthorizedAuthenticationProvider.
 * <p>
 * The SessionController is used for initially getting the cookie in exchange for a valid CognitoToken
 */
@Slf4j
public class PreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private SessionService sessionService;
    private PortalUserService portalUserService;

    public PreAuthenticationFilter(SessionService sessionService, PortalUserService portalUserService) {
        this.sessionService = sessionService;
        this.portalUserService = portalUserService;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                    Optional<Session> activeSessionOptional = sessionService.findActiveSession(cookie.getValue());
                    if (activeSessionOptional.isPresent()) {
                        Session session = activeSessionOptional.get();
                        Optional<PortalUser> portalUserOptional = portalUserService.findById(session.getPortalUserId(), session.getOrganizationId());
                        if (portalUserOptional.isPresent()) {
                            log.info("Adding portal user " + portalUserOptional.get().getId() + " to security context");
                            return SecurityContextHelper.getPreAuthenticatedPrincipal(portalUserOptional.get());
                        } else {
                            throw new EntityNotFoundException(PortalUser.class, session.getPortalUserId());
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "";
    }

}
