package com.obaccelerator.portal.auth.spring;

import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.session.Session;
import com.obaccelerator.portal.shared.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static com.obaccelerator.common.ObaConstant.ROLE_OBA_ANONYMOUS;
import static com.obaccelerator.common.ObaConstant.ROLE_PORTAL_ORGANIZATION;
import static com.obaccelerator.portal.ObaPortalApplication.SESSION_COOKIE_NAME;

@Slf4j
public class CookiePreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final SessionService sessionService;
    private final PortalUserService portalUserService;

    public CookiePreAuthenticationFilter(SessionService sessionService, PortalUserService portalUserService) {
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
                            log.debug("Found logged in user with id " + portalUserOptional.get().getId() + ". Updating session last used time");
                            sessionService.updateSessionLastUsed(session.getId());
                            return authenticationWithRole(ROLE_PORTAL_ORGANIZATION);
                        }
                    }
                }
            }
        }
        return authenticationWithRole(ROLE_OBA_ANONYMOUS);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "";
    }

    private PreAuthenticatedAuthenticationToken authenticationWithRole(String role) {
        return new PreAuthenticatedAuthenticationToken((Principal) () -> "oba", null,
                Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
