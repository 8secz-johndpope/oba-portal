package com.obaccelerator.portal.session;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.auth.cognito.CognitoService;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.shared.session.NoSessionException;
import com.obaccelerator.portal.shared.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.obaccelerator.portal.ObaPortalApplication.SESSION_COOKIE_NAME;


@Slf4j
@RestController
public class SessionController {

    private final SessionService sessionService;
    private final PortalUserService portalUserService;
    private final CognitoService cognitoService;
    private final FirstTimeSessionService firstTimeSessionService;
    private ObaPortalProperties obaPortalProperties;

    public SessionController(SessionService sessionService, PortalUserService portalUserService,
                             CognitoService cognitoService, FirstTimeSessionService firstTimeSessionService,
                             ObaPortalProperties obaPortalProperties) {
        this.sessionService = sessionService;
        this.portalUserService = portalUserService;
        this.cognitoService = cognitoService;
        this.firstTimeSessionService = firstTimeSessionService;
        this.obaPortalProperties = obaPortalProperties;
    }

    /**
     * Checks whether the user has a valid session cookie. This is called from the Angular front-end's AuthGuard
     * whenever the user navigates to an Admin view.
     *
     * @param portalSessionId
     * @return
     */
    @GetMapping("/sessions")
    public Session getActiveSession(@CookieValue(value = "oba_portal_session", required = false) String portalSessionId) {
        if(portalSessionId == null) {
            throw new NoSessionException();
        }
        return sessionService.findActiveSession(portalSessionId).orElseThrow(NoSessionException::new);
    }

    @PostMapping("/sessions")
    public Session cognitoTokenToSession(HttpServletRequest request, HttpServletResponse response) {
        // Validate the token and get its claims
        Map<String, Object> tokenClaims = cognitoService.verifyAndGetCognitoClaimsFromRequest(request);
        // Find an existing user or create it and set it on the token as 'details'
        Optional<PortalUser> portalUserOptional = portalUserService.findByCognitoId((String) tokenClaims.get("sub"));
        // If the user doesn't exist, create it
        PortalUser portalUser = portalUserOptional.orElseGet(() -> firstTimeSessionService.promoteRegistrationToOrganizationWithUser(tokenClaims));
        SecurityContextHelper.registerPortalUserWithSpringSecurityContext(portalUser);
        Session session = sessionService.createSession(portalUser.getId());
        setSessionCookie(session.getId(), response);
        log.info("Returning session with id " + session.getId());
        return session;
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION')")
    @DeleteMapping("/sessions")
    public void deleteSession(@CookieValue(value = "oba_portal_session") String portalSessionId) {
        UUID uuid = UUIDParser.fromString(portalSessionId);
        sessionService.deleteSession(uuid);
    }

    /**
     * Creates the session cookie and ads it to the response. There is no max age set for the cookie. The session
     * will expire in the backend if it is not used for more than 30 minutes
     *
     * @param session
     * @param response
     */
    private void setSessionCookie(UUID session, HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.toString());
        cookie.setDomain(obaPortalProperties.getApplicationDomain());
        cookie.setPath(obaPortalProperties.getApplicationContextPath());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
