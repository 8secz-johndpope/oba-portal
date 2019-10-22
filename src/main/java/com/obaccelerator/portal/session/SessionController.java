package com.obaccelerator.portal.session;

import com.obaccelerator.portal.cognito.CognitoService;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.portaluser.PortalUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    @PostMapping("/sessions")
    public void cognitoTokenToSession(@Valid @RequestBody CognitoIdToken cognitoToken, HttpServletResponse response) {
        // Validate the Cognito token signature and get its claims
        Map<String, Object> tokenClaims = cognitoService.verifyAndGetCognitoClaims(cognitoToken);

        String cognitoId = tokenClaims.get("sub").toString();
        Optional<PortalUser> portalUserOptional = portalUserService.findByCognitoId(cognitoId);

        // The user has successfully authenticated with Cognito and OBA was able to identify the user, so
        // we return a session cookie
        if (portalUserOptional.isPresent()) {
            setSessionCookie(portalUserOptional.get(), response);
            return;
        }

        // If OBA does not know the user yet we should look for a registration and promote it to an organization
        PortalUser portalUser = firstTimeSessionService.promoteRegistrationToOrganizationWithUser(tokenClaims);
        setSessionCookie(portalUser, response);
    }

    // TODO: Security : sign cookie content or use the Cognito token and verify signature
    private void setSessionCookie(PortalUser portalUser, HttpServletResponse response) {
        UUID session = sessionService.createSession(portalUser.getId());
        Cookie cookie = new Cookie("oba_portal_session", session.toString());
        cookie.setDomain("localhost");
        response.addCookie(cookie);
    }
}
