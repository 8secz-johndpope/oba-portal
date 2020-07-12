package com.obaccelerator.portal.application;

import com.obaccelerator.common.token.KeyUtil;
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

    @GetMapping("/applications/{applicationId}")
    public Application findApplication(PortalUser portalUser, @PathVariable UUID applicationId) {
        return applicationGatewayService.findApplication(portalUser.getOrganizationId(), applicationId);
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
        KeyUtil.stringToRsaPublicKey(request.getPublicKey());
        return applicationGatewayService.createApplicationPublicKey(portalUser.getOrganizationId(), applicationId, request);
    }

    @DeleteMapping("/applications/{applicationId}/public-keys/{publicKeyId}")
    public void deleteApplicationPublicKey(PortalUser portalUser,
                                           @PathVariable UUID applicationId,
                                           @PathVariable UUID publicKeyId) {
        applicationGatewayService.deleteApplicationPublicKey(portalUser.getOrganizationId(), applicationId, publicKeyId);
    }

    @GetMapping("/applications/{applicationId}/available-country-data-providers")
    public List<AvailableCountryDataProvider> findAvailableWithEnabledProjection(PortalUser portalUser,
                                                                                 @PathVariable UUID applicationId) {
        return applicationGatewayService.findAvailableWithEnabledProjection(portalUser.getOrganizationId(), applicationId);
    }

    @PostMapping("/applications/{applicationId}/enabled-country-data-providers")
    public EnabledCountryDataProvider enableApi(PortalUser portalUser,
                                                @PathVariable UUID applicationId,
                                                @RequestBody @Valid EnableCountryDataProviderRequest enableCountryDataProviderRequest) {
        return applicationGatewayService.createEnabledCountryDataProvider(portalUser.getOrganizationId(), applicationId, enableCountryDataProviderRequest);
    }

    @DeleteMapping("/applications/{applicationId}/enabled-country-data-providers/{systemName}")
    public void disableApi(PortalUser portalUser,
                                 @PathVariable UUID applicationId,
                                 @PathVariable String systemName) {
        applicationGatewayService.deleteEnabledCountryDataProvider(portalUser.getOrganizationId(), applicationId, systemName);
    }

}
