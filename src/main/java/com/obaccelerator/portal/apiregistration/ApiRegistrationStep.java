package com.obaccelerator.portal.apiregistration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class ApiRegistrationStep extends RegistrationError {
    UUID id;
    int stepNr;
    UUID apiId;
    UUID signingCertificateId;
    UUID transportCertificateId;
    OffsetDateTime created;

    String message;
    String code;
}
