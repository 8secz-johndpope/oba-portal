package com.obaccelerator.portal.apiregistration;

import com.obaccelerator.common.model.certificate.KeyPurpose;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class OrganizationCertificate {
    UUID id;
    UUID organizationId;
    String name;
    String description;
    KeyPurpose keyPurpose;
    String csrDistinguishedName;
    String csr;
    String certificateDistinguishedName;
    String signedCertificate;
    OffsetDateTime validFrom;
    OffsetDateTime validUntil;
    int daysUntilExpiration;
    OffsetDateTime created;
}
