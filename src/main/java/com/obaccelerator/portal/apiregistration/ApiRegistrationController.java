package com.obaccelerator.portal.apiregistration;

import com.obaccelerator.common.form.SubmittedForm;
import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/api-registration-steps/{apiId}")
    public ApiRegistrationStep submitStep(PortalUser portalUser,
                                          @PathVariable("apiId") UUID apiId,
                                          @RequestBody @Valid SubmittedForm submittedForm) {
        return apiRegistrationGatewayService.submitRegistrationStep(portalUser.getOrganizationId(), apiId, submittedForm);
    }
}
