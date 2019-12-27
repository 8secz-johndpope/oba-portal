package com.obaccelerator.portal.organization;

import com.obaccelerator.common.endpoint.EndpointDef;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrganizationController {

    private OrganizationObaGatewayService organizationObaGatewayService;

    public OrganizationController(OrganizationObaGatewayService organizationObaGatewayService) {
        this.organizationObaGatewayService = organizationObaGatewayService;
    }

    @GetMapping(EndpointDef.Path.GET_ORGANIZATION)
    public ObaOrganization getOrganization(UUID id) {
        return this.organizationObaGatewayService.findOrganization(id);
    }
}
