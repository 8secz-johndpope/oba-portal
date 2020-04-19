package com.obaccelerator.portal.organization;

import com.obaccelerator.portal.session.Session;
import com.obaccelerator.portal.shared.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrganizationController {

    private OrganizationObaGatewayService organizationObaGatewayService;
    private SessionService sessionService;

    public OrganizationController(OrganizationObaGatewayService organizationObaGatewayService, SessionService sessionService) {
        this.organizationObaGatewayService = organizationObaGatewayService;
        this.sessionService = sessionService;
    }

    @PreAuthorize("hasRole('portal_user')")
    @GetMapping("/organizations/{organizationId}")
    public ObaOrganization getOrganization(@CookieValue(value = "oba_portal_session", required = false) String portalSessionId) {
        Session activeSession = sessionService.findActiveSession(portalSessionId);
        return organizationObaGatewayService.findOrganization(activeSession.getOrganizationId());
    }
}
