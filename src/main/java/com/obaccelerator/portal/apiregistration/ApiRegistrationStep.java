package com.obaccelerator.portal.apiregistration;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class ApiRegistrationStep {
    UUID id;
    int stepNr;
    UUID apiId;
    UUID signingCertificateId;
    UUID transportCertificateId;
    OffsetDateTime created;
}
