package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.model.certificate.KeyPurpose;
import com.obaccelerator.common.rest.RestResponse;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class CreateCertificateResponse extends RestResponse {
    UUID id;
    UUID organizationId;
    String name;
    String description;
    KeyPurpose keyPurpose;
    String distinguishedName;
    String csr;
    String signedCertificate;
    OffsetDateTime created;
}
