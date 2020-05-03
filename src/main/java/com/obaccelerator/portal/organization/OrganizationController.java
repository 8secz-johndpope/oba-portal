package com.obaccelerator.portal.organization;

import com.obaccelerator.common.uuid.UUIDParser;
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
    public ObaOrganization getOrganization(@PathVariable("organizationId") String organizationId) {
        return organizationObaGatewayService.findOrganization(UUIDParser.fromString(organizationId));
    }
}
