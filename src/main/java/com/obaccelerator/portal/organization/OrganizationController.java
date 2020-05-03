package com.obaccelerator.portal.organization;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.auth.NotAuthorizedException;
import com.obaccelerator.portal.portaluser.PortalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrganizationController {

    private OrganizationObaGatewayService organizationObaGatewayService;

    public OrganizationController(OrganizationObaGatewayService organizationObaGatewayService) {
        this.organizationObaGatewayService = organizationObaGatewayService;
    }

    @GetMapping("/organizations/{organizationId}")
    public ObaOrganization getOrganization(@PathVariable("organizationId") String organizationId, PortalUser portalUser) {
        authorize(portalUser, organizationId);
        return organizationObaGatewayService.findOrganization(UUIDParser.fromString(organizationId));
    }

    private void authorize(PortalUser portalUser, String organizationId) {
        if (!portalUser.belongsToOrganization(organizationId)) {
            throw new NotAuthorizedException();
        }
    }
}
