package com.obaccelerator.portal.application;

import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ORGANIZATION')")
@RestController
public class ApplicationController {

    private final ApplicationGatewayService applicationGatewayService;

    public ApplicationController(ApplicationGatewayService applicationGatewayService) {
        this.applicationGatewayService = applicationGatewayService;
    }

    @GetMapping("/applications")
    public List<Application> findApplications(PortalUser portalUser) {
        return applicationGatewayService.findApplications(portalUser.getOrganizationId());
    }

    @PostMapping("/applications")
    public Application createApplication(PortalUser portalUser,
                                         @RequestBody CreateApplicationRequest createApplicationRequest) {
        return applicationGatewayService.createApplication(portalUser.getOrganizationId(), createApplicationRequest);
    }

    @DeleteMapping("/applications/{applicationId}")
    public void deleteApplication(PortalUser portalUser, @PathVariable UUID applicationId) {
        applicationGatewayService.deleteApplication(portalUser.getOrganizationId(), applicationId);
    }

    @GetMapping("/applications/{applicationId}/public-keys")
    public List<ApplicationPublicKey> findApplicationPublicKeys(PortalUser portalUser,
                                                                @PathVariable UUID applicationId) {
        return applicationGatewayService.findApplicationPublicKeys(portalUser.getOrganizationId(), applicationId);
    }

    @PostMapping("/applications/{applicationId}/public-keys")
    public ApplicationPublicKey createApplicationPublicKey(PortalUser portalUser,
                                                           @PathVariable UUID applicationId,
                                                           @RequestBody @Valid CreateApplicationPublicKeyRequest request) {
        return applicationGatewayService.createApplicationPublicKey(portalUser.getOrganizationId(), applicationId, request);
    }

}
