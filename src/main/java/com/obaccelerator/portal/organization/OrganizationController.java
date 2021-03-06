package com.obaccelerator.portal.organization;

import com.obaccelerator.common.model.certificate.KeyPurpose;
import com.obaccelerator.portal.certificate.CertificateListResponse;
import com.obaccelerator.portal.certificate.CertificateObaGatewayService;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.redirecturl.RedirectUrlGatewayService;
import com.obaccelerator.portal.redirecturl.RedirectUrlResponse;
import com.obaccelerator.portal.redirecturl.RedirectUrlWithNumberOfRegistrations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@PreAuthorize("hasRole('ROLE_ORGANIZATION')")
@RestController
public class OrganizationController {

    private final OrganizationObaGatewayService organizationObaGatewayService;
    private final RedirectUrlGatewayService redirectUrlGatewayService;
    private final CertificateObaGatewayService certificateObaGatewayService;

    public OrganizationController(OrganizationObaGatewayService organizationObaGatewayService, RedirectUrlGatewayService redirectUrlGatewayService, CertificateObaGatewayService certificateObaGatewayService) {
        this.organizationObaGatewayService = organizationObaGatewayService;
        this.redirectUrlGatewayService = redirectUrlGatewayService;
        this.certificateObaGatewayService = certificateObaGatewayService;
    }

    @GetMapping("/organizations/{organizationId}")
    public ObaOrganizationResponse getOrganization(PortalUser portalUser) {
        return organizationObaGatewayService.findOrganization(portalUser.getOrganizationId());
    }

    @PutMapping("/organizations/{organizationId}")
    public ObaOrganizationResponse updateOrganization(@Valid @RequestBody UpdateObaOrganizationRequest updateObaOrganizationRequest,
                                                      PortalUser portalUser) {
        return organizationObaGatewayService.updateOrganization(updateObaOrganizationRequest, portalUser);
    }

    @GetMapping("/organizations/completeness-report")
    public CompletenessReport organizationComplete(PortalUser portalUser) {
        ObaOrganizationResponse organization = organizationObaGatewayService.findOrganization(portalUser.getOrganizationId());
        List<RedirectUrlWithNumberOfRegistrations> redirectUrls = redirectUrlGatewayService.findAllForOrganization(portalUser.getOrganizationId());
        CertificateListResponse certificates = certificateObaGatewayService.findAllForOrganization(portalUser.getOrganizationId(), false);
        boolean signingExists = certificates.stream().anyMatch(c -> c.getKeyPurpose().equals(KeyPurpose.SIGNING));
        boolean signingValid = certificates.stream().anyMatch(c -> c.getKeyPurpose().equals(KeyPurpose.SIGNING) && c.getValidUntil().isAfter(OffsetDateTime.now()));
        boolean transportExists = certificates.stream().anyMatch(c -> c.getKeyPurpose().equals(KeyPurpose.TRANSPORT));
        boolean transportValid = certificates.stream().anyMatch(c -> c.getKeyPurpose().equals(KeyPurpose.TRANSPORT) && c.getValidUntil().isAfter(OffsetDateTime.now()));
        return new CompletenessReport(!redirectUrls.isEmpty(), organization.isComplete(), signingExists, signingValid, transportExists, transportValid);
    }

}
