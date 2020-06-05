package com.obaccelerator.portal.organization;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.auth.NotAuthorizedException;
import com.obaccelerator.portal.portaluser.PortalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class OrganizationController {

    private final OrganizationObaGatewayService organizationObaGatewayService;

    public OrganizationController(OrganizationObaGatewayService organizationObaGatewayService) {
        this.organizationObaGatewayService = organizationObaGatewayService;
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @GetMapping("/organizations/{organizationId}")
    public ObaOrganizationResponse getOrganization(@PathVariable("organizationId") String organizationId, PortalUser portalUser) {
        authorize(portalUser, organizationId);
        return organizationObaGatewayService.findOrganization(UUIDParser.fromString(organizationId));
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @PutMapping("/organizations")
    public ObaOrganizationResponse updateOrganization(@Valid @RequestBody UpdateObaOrganizationRequest updateObaOrganizationRequest,
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
