package com.obaccelerator.portal.certificate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CertificateController {

    private CertificateObaGatewayService certificateObaGatewayService;

    public CertificateController(CertificateObaGatewayService certificateObaGatewayService) {
        this.certificateObaGatewayService = certificateObaGatewayService;
    }

    // TODO: security - integrate Spring security and use pre-authenticated scenario like in Oba-Portal. Get rid of endpoint definitions in commons
    // TODO: security - implement a signed portal token that has am organizationId claim
    @PostMapping("/{organizationId}/certificates")
    public CreateCertificateResponse createCertificate(@Valid @RequestBody CreateOrganizationCertificateRequest request,
                                                       @PathVariable String organizationId) {
        return certificateObaGatewayService.createCertificateInOba(request, organizationId);
    }
}
