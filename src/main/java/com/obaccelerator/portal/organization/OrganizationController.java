package com.obaccelerator.portal.organization;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.auth.NotAuthorizedException;
import com.obaccelerator.portal.portaluser.PortalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class OrganizationController {

    private final OrganizationObaGatewayService organizationObaGatewayService;

    public OrganizationController(OrganizationObaGatewayService organizationObaGatewayService) {
        this.organizationObaGatewayService = organizationObaGatewayService;
    }

    @GetMapping("/organizations/{organizationId}")
    public ObaOrganization getOrganization(@PathVariable("organizationId") String organizationId, PortalUser portalUser) {
        authorize(portalUser, organizationId);
        return organizationObaGatewayService.findOrganization(UUIDParser.fromString(organizationId));
    }

    @PutMapping("/organizations")
    public ObaOrganization updateOrganization(@Valid @RequestBody UpdateObaOrganizationRequest updateObaOrganizationRequest,
                                              PortalUser portalUser) {
        authorize(portalUser, updateObaOrganizationRequest.getOrganizationId());
        return organizationObaGatewayService.updateOrganization(updateObaOrganizationRequest);
    }

    private void authorize(PortalUser portalUser, String organizationId) {
        if (!portalUser.belongsToOrganization(organizationId)) {
            throw new NotAuthorizedException();
        }
    }
}
