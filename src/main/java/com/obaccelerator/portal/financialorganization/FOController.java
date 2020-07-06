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

    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION', 'ROLE_APPLICATION')")
    @GetMapping("/financial-organizations")
    public FOListResponse banksList(PortalUser portalUser) {
        return FinancialOrganizationGatewayService.findFinancialOrganizations(portalUser.getOrganizationId());
    }

    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION', 'ROLE_APPLICATION')")
    @GetMapping("/financial-organizations/{bankSystemName}")
    public FinancialOrganization bank(PortalUser portalUser,
                                      @PathVariable("bankSystemName") String bankSystemName) {
        return FinancialOrganizationGatewayService.findFinancialOrganization(portalUser.getOrganizationId(), bankSystemName);
    }
}
