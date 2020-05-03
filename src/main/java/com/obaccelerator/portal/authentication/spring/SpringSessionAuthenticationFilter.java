package com.obaccelerator.portal.authentication.spring;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.session.SecurityContextHelper;
import com.obaccelerator.portal.session.Session;
import com.obaccelerator.portal.shared.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.obaccelerator.portal.ObaPortalApplication.SESSION_COOKIE_NAME;

/**
 * This is a regular filter that checks every request for a session cookie. If found, the user to whom the session belongs
 * is retrieved and added to the Spring Security Context as a user with role ORGANIZATION_ADMIN.
 *
 * There are maybe more elegant ways to integrate with Spring Security, such as AbstractPreAuthenticatedProcessingFilter
 * or AbstractAuthenticationProcessingFilter. I have tried a few option, but found this to be simple, readable and
 * effective.
 *
 * I am not sure Spring Security offers classes for my specific approach : authentication at Cognito yields a session
 * token, that is then validated by Oba Portal (signature validation) and is then exchanged for an Oba Portal session
 * cookie. So I decided to write this barebones filter for checking each request and the POST /sessions endpoint for
 * the token-to-session exchange.
 *
 * This filter runs for every request, so also for requests to POST /sessions. This is not a problem. Since no cookie
 * is present before authentication at POST /sessions the filter will skip its session logic and proceed as normal.
 */
@Slf4j
public class SpringSessionAuthenticationFilter extends OncePerRequestFilter {


    private SessionService sessionService;
    private PortalUserService portalUserService;

    public SpringSessionAuthenticationFilter(SessionService sessionService, PortalUserService portalUserService) {
        this.sessionService = sessionService;
        this.portalUserService = portalUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Custom security filter hit");
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                    Optional<Session> activeSessionOptional = sessionService.findActiveSession(cookie.getValue());
                    if(activeSessionOptional.isPresent()) {
                        Session session = activeSessionOptional.get();
                        Optional<PortalUser> portalUserOptional = portalUserService.findById(session.getPortalUserId(), session.getOrganizationId());
                        if(portalUserOptional.isPresent()) {
                            log.info("Adding portal user " + portalUserOptional.get().getId() + " to security context");
                            SecurityContextHelper.portalUserToSecurityContext(portalUserOptional.get());
                        } else {
                            throw new EntityNotFoundException(PortalUser.class, session.getPortalUserId());
                        }
                    }
                }
            }
        }

        filterChain.doFilter(request, response);

    }


}
