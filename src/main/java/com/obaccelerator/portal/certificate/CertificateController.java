package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.uuid.UUIDParser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CertificateController {

    private final CertificateObaGatewayService certificateObaGatewayService;

    public CertificateController(CertificateObaGatewayService certificateObaGatewayService) {
        this.certificateObaGatewayService = certificateObaGatewayService;
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @PostMapping("/organization/{organizationId}/certificates")
    public CertificateResponse create(@Valid @RequestBody CreateOrganizationCertificateRequest request,
                                      @PathVariable String organizationId) {
        return certificateObaGatewayService.createCertificateInOba(request, UUIDParser.fromString(organizationId));
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @GetMapping("/organization/{organizationId}/certificates")
    public CertificateListResponse findAll(@PathVariable String organizationId) {
        return certificateObaGatewayService.findAllForOrganization(UUIDParser.fromString(organizationId));
    }

    @PreAuthorize("hasAuthority('portal_organization')")
    @GetMapping("/organization/{organizationId}/certificates/{certificateId}")
    public CertificateResponse findOne(@PathVariable String organizationId, @PathVariable String certificateId) {
        return certificateObaGatewayService.findOneForOrganization(UUIDParser.fromString(organizationId),
                UUIDParser.fromString(certificateId));
    }
}
