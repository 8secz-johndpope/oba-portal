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
    public List<ApiRegistrationStepDefinition> findRegistrationAttemps(PortalUser portalUser,
                                                                       @PathVariable("apiId") UUID apiId) {
        return null;
    }

    @GetMapping("/api-registrations")
    public List<ApiRegistrationStepResult> findApiRegistrationStepResults(PortalUser portalUser,
                                                                          @PathVariable("apiId") UUID apiId) {
        return null;
    }


}
