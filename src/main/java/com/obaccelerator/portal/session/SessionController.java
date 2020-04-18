package com.obaccelerator.portal.session;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.cognito.CognitoService;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;


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

    /**
     * Checks whether the user has a valid session cookie
     *
     * @param portalSessionId
     * @return
     */
    @GetMapping("/sessions")
    public ResponseEntity<Session> getActiveSession(@CookieValue(value = "oba_portal_session", required = false) String portalSessionId) {
        if (isBlank(portalSessionId)) {
            log.info("No session id present");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        log.info("Returning session");
        return new ResponseEntity<>(sessionService.findActiveSession(portalSessionId), HttpStatus.OK);
    }

    @PostMapping("/sessions")
    public void cognitoTokenToSession(@Valid @RequestBody CognitoIdToken cognitoToken, HttpServletResponse response) {
        // Validate the Cognito token signature and get its claims
        Map<String, Object> tokenClaims = cognitoService.verifyAndGetCognitoClaims(cognitoToken);

        String cognitoId = tokenClaims.get("sub").toString();
        Optional<PortalUser> portalUserOptional = portalUserService.findByCognitoId(cognitoId);
        PortalUser portalUser;
        // The user has successfully authenticated with Cognito and OBA was able to identify the user..
        // If OBA does not know the user yet we should look for a registration and promote it to an organization
        portalUser = portalUserOptional.orElseGet(() -> firstTimeSessionService.promoteRegistrationToOrganizationWithUser(tokenClaims));
        UUID session = sessionService.createSession(portalUser.getId());
        setSessionCookie(session, response);
    }

    @DeleteMapping
    public void deleteSession(@CookieValue(value = "oba_portal_session", required = false) String portalSessionId) {
        UUID uuid = UUIDParser.fromString(portalSessionId);
        sessionService.deleteSession(uuid);
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
