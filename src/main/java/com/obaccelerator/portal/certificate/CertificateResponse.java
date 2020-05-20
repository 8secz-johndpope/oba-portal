package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.model.certificate.KeyPurpose;
import com.obaccelerator.common.rest.RestResponse;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class CertificateResponse extends RestResponse {
    UUID id;
    UUID organizationId;
    String name;
    String description;
    KeyPurpose keyPurpose;
    String csrDistinguishedName;
    String csr;
    String certificateDistinguishedName;
    String signedCertificate;
    OffsetDateTime created;
    OffsetDateTime validFrom;
    OffsetDateTime validUntil;
    long daysUntilExpiration;
}
