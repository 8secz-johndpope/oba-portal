package com.obaccelerator.portal.apiregistration;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ORGANIZATION')")
@RestController
public class ApiRegistrationController {

    private final ApiRegistrationGatewayService apiRegistrationGatewayService;

    public ApiRegistrationController(ApiRegistrationGatewayService apiRegistrationGatewayService) {
        this.apiRegistrationGatewayService = apiRegistrationGatewayService;
    }

    @GetMapping("/api-registrations/{apiId}")
    public List<ApiRegistration> findApiRegistrationsForApi(PortalUser portalUser,
                                                            @PathVariable("apiId") UUID apiId) {
        return apiRegistrationGatewayService.findApiRegistrations(new ByOrganizationAndApi(portalUser.getOrganizationId(), apiId));
    }

    @GetMapping("/api-registrations")
    public List<ApiRegistration> findApiRegistrationsForOrganization(PortalUser portalUser) {
        return apiRegistrationGatewayService.findRegistrationsForOrganization(portalUser.getOrganizationId());
    }

    @GetMapping("/api-registration-steps/{apiId}")
    public ApiRegistrationSteps findApiRegistrationStepResults(PortalUser portalUser, @PathVariable("apiId") UUID apiId) {
        return apiRegistrationGatewayService
                .findApiRegistrationSteps(new ByOrganizationAndApi(portalUser.getOrganizationId(), apiId));
    }
}
