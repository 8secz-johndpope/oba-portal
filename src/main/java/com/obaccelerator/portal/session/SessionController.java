package com.obaccelerator.portal.session;

import com.obaccelerator.portal.cognito.CognitoService;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class SessionController {

    private SessionService sessionService;
    private PortalUserService portalUserService;
    private CognitoService cognitoService;
    private FirstTimeSessionService firstTimeSessionService;

    public SessionController(SessionService sessionService, PortalUserService portalUserService,
                             CognitoService cognitoService, FirstTimeSessionService firstTimeSessionService) {
        this.sessionService = sessionService;
        this.portalUserService = portalUserService;
        this.cognitoService = cognitoService;
        this.firstTimeSessionService = firstTimeSessionService;
    }

    @GetMapping("/sessions")
    public Session getActiveSession(@CookieValue(value = "oba_portal_session", required = false) String portalSessionId) {
        return sessionService.findActiveSession(portalSessionId);
    }

    @PostMapping("/sessions")
    public void cognitoTokenToSession(@Valid @RequestBody CognitoIdToken cognitoToken, HttpServletResponse response) {
        // Validate the Cognito token signature and get its claims
        Map<String, Object> tokenClaims = cognitoService.verifyAndGetCognitoClaims(cognitoToken);

        String cognitoId = tokenClaims.get("sub").toString();
        Optional<PortalUser> portalUserOptional = portalUserService.findByCognitoId(cognitoId);
        PortalUser portalUser;
        // The user has successfully authenticated with Cognito and OBA was able to identify the user..
        if (portalUserOptional.isPresent()) {
            portalUser = portalUserOptional.get();
        } else {
            // If OBA does not know the user yet we should look for a registration and promote it to an organization
            portalUser = firstTimeSessionService.promoteRegistrationToOrganizationWithUser(tokenClaims);
        }

        UUID session = sessionService.createSession(portalUser.getId());
        setSessionCookie(session, response);
    }

    // TODO: Security : sign cookie content or use the Cognito token and verify signature
    private void setSessionCookie(UUID session, HttpServletResponse response) {
        log.info("Returning session {}", session);
        Cookie cookie = new Cookie("oba_portal_session", session.toString());
        cookie.setDomain("oba-portal.com");
        cookie.setPath("*");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }
}
