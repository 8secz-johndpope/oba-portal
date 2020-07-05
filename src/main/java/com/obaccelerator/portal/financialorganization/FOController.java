package com.obaccelerator.portal.financialorganization;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FOController {

    private final FOObaGatewayService FOObaGatewayService;

    public FOController(FOObaGatewayService FOObaGatewayService) {
        this.FOObaGatewayService = FOObaGatewayService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION', 'ROLE_APPLICATION')")
    @GetMapping("/banks")
    public FOListResponse banksList(PortalUser portalUser) {
        return FOObaGatewayService.findBanks(portalUser.getOrganizationId());
    }

    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION', 'ROLE_APPLICATION')")
    @GetMapping("/banks/{bankSystemName}")
    public FinancialOrganization bank(PortalUser portalUser,
                                      @PathVariable("bankSystemName") String bankSystemName) {
        return FOObaGatewayService.findBank(portalUser.getOrganizationId(), bankSystemName);
    }
}
