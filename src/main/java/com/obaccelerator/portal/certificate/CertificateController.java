package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.uuid.UUIDParser;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CertificateController {

    private final CertificateObaGatewayService certificateObaGatewayService;

    public CertificateController(CertificateObaGatewayService certificateObaGatewayService) {
        this.certificateObaGatewayService = certificateObaGatewayService;
    }

    // TODO: security - integrate Spring security and use pre-authenticated scenario like in Oba-Portal. Get rid of endpoint definitions in commons
    // TODO: security - implement a signed portal token that has am organizationId claim
    @PostMapping("/{organizationId}/certificates")
    public CertificateResponse create(@Valid @RequestBody CreateOrganizationCertificateRequest request,
                                      @PathVariable String organizationId) {
        return certificateObaGatewayService.createCertificateInOba(request, organizationId);
    }

    @GetMapping("/{organizationId}/certificates")
    public CertificateListResponse findAll(@PathVariable String organizationId) {
        return certificateObaGatewayService.findAll(UUIDParser.fromString(organizationId));
    }
}
