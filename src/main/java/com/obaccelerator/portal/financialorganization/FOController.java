package com.obaccelerator.portal.financialorganization;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FOController {

    private final FinancialOrganizationGatewayService FinancialOrganizationGatewayService;

    public FOController(FinancialOrganizationGatewayService FinancialOrganizationGatewayService) {
        this.FinancialOrganizationGatewayService = FinancialOrganizationGatewayService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION')")
    @GetMapping("/financial-organizations")
    public FOListResponse financialOrganizationsList(PortalUser portalUser) {
        return FinancialOrganizationGatewayService.findFinancialOrganizations(portalUser.getOrganizationId());
    }

    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION')")
    @GetMapping("/financial-organizations/{systemName}")
    public FinancialOrganization financialOrganization(PortalUser portalUser,
                                                       @PathVariable("systemName") String systemName) {
        return FinancialOrganizationGatewayService.findFinancialOrganization(portalUser.getOrganizationId(), systemName);
    }
}
