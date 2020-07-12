package com.obaccelerator.portal.api;

import com.obaccelerator.portal.apiregistration.ByOrganizationAndApi;
import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@PreAuthorize("hasRole('ROLE_ORGANIZATION')")
@RestController
public class ApiController {

    private final ApiGatewayService apiGatewayService;

    public ApiController(ApiGatewayService apiGatewayService) {
        this.apiGatewayService = apiGatewayService;
    }


    @GetMapping("/apis/{apiId}")
    public ApiWithCountryDataProviders findById(PortalUser portalUser, @PathVariable("apiId") UUID apiId) {
        return apiGatewayService.findOneApiWithRegistrations(new ByOrganizationAndApi(portalUser.getOrganizationId(), apiId));
    }

    @GetMapping("/apis")
    public List<ApiWithCountryDataProviders> findWithRegistrations(PortalUser portalUser) {
        return apiGatewayService.findAllApisWithRegistrations(portalUser.getOrganizationId());
    }
}
