package com.obaccelerator.portal.api;

import com.obaccelerator.portal.apiregistration.ByOrganizationAndApi;
import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
public class ApiController {

    private final ApiGatewayService apiGatewayService;

    public ApiController(ApiGatewayService apiGatewayService) {
        this.apiGatewayService = apiGatewayService;
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION')")
    @GetMapping("/apis/{apiId}")
    public Api findById(PortalUser portalUser,  @PathVariable("apiId") UUID apiId) {
        return apiGatewayService.findApi(new ByOrganizationAndApi(portalUser.getOrganizationId(), apiId));
    }
}
