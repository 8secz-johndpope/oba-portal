package com.obaccelerator.portal.bank;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BankController {

    private final BankObaGatewayService bankObaGatewayService;

    public BankController(BankObaGatewayService bankObaGatewayService) {
        this.bankObaGatewayService = bankObaGatewayService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ORGANIZATION', 'ROLE_APPLICATION')")
    @GetMapping("/banks")
    public BankListResponse banksList(PortalUser portalUser) {
        return bankObaGatewayService.findBanks(portalUser.getOrganizationId());
    }
}
