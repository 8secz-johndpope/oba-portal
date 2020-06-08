package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.uuid.UUIDParser;
import com.obaccelerator.portal.portaluser.PortalUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasRole('ROLE_ORGANIZATION')")
@RestController
public class CertificateController {

    private final CertificateObaGatewayService certificateObaGatewayService;

    public CertificateController(CertificateObaGatewayService certificateObaGatewayService) {
        this.certificateObaGatewayService = certificateObaGatewayService;
    }

    @PostMapping("/certificates")
    public CertificateResponse create(@Valid @RequestBody CreateOrganizationCertificateRequest request, PortalUser portalUser) {
        return certificateObaGatewayService.createCertificateInOba(request, portalUser.getOrganizationId());
    }

    @GetMapping("/certificates")
    public CertificateListResponse findAll(PortalUser portalUser,
                                           @RequestParam(value = "nonExpiredOnly", required = false) boolean nonExpired) {
        return certificateObaGatewayService.findAllForOrganization(portalUser.getOrganizationId(), nonExpired);
    }

    @GetMapping("/certificates/{certificateId}")
    public CertificateResponse findOne(@PathVariable String certificateId, PortalUser portalUser) {
        return certificateObaGatewayService.findOneForOrganization(portalUser.getOrganizationId(),
                UUIDParser.fromString(certificateId));
    }
}
