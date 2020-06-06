package com.obaccelerator.portal.organization;

import com.obaccelerator.portal.portaluser.PortalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ObaOrganizationResponse getOrganization(PortalUser portalUser) {
        return organizationObaGatewayService.findOrganization(portalUser.getOrganizationId());
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @PutMapping("/organizations")
    public ObaOrganizationResponse updateOrganization(@Valid @RequestBody UpdateObaOrganizationRequest updateObaOrganizationRequest,
                                                      PortalUser portalUser) {
        return organizationObaGatewayService.updateOrganization(updateObaOrganizationRequest);
    }

}
