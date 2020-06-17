package com.obaccelerator.portal.apiregistration;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class ApiRegistrationStepResult {
    UUID id;
    int stepNr;
    UUID apiId;
    UUID signingCertificateId;
    UUID transportCertificateId;
    boolean success;
    OffsetDateTime created;
}
