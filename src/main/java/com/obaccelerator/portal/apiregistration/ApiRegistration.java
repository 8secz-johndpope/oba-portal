package com.obaccelerator.portal.apiregistration;

import com.obaccelerator.portal.certificate.CertificateResponse;
import com.obaccelerator.portal.redirecturl.RedirectUrlResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ApiRegistration {
    UUID id;
    UUID organizationId;
    UUID apiId;
    CertificateResponse signingCertificate;
    CertificateResponse tlsCertificate;
    boolean enabled;
    OffsetDateTime created;
    List<RedirectUrlResponse> redirectUrls;
}
