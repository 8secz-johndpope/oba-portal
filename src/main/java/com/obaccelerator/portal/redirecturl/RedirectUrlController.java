package com.obaccelerator.portal.redirecturl;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedirectUrlController {

    private final RedirectUrlGatewayService redirectUrlGatewayService;

    public RedirectUrlController(RedirectUrlGatewayService redirectUrlGatewayService) {
        this.redirectUrlGatewayService = redirectUrlGatewayService;
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @GetMapping("/redirect-urls/{organizationId}")
    public List<RedirectUrl> getRedirectUrlsForOrganization(PortalUser portalUser) {
        return redirectUrlGatewayService.findAllForOrganization(portalUser.getOrganizationId());
    }
}
