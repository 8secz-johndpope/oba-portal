package com.obaccelerator.portal.apiregistration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.obaccelerator.portal.certificate.CertificateResponse;
import com.obaccelerator.portal.redirecturl.RedirectUrlResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ApiRegistration extends RegistrationError {
    UUID id;
    UUID organizationId;
    UUID apiId;
    CertificateResponse signingCertificate;
    CertificateResponse tlsCertificate;
    boolean enabled;
    OffsetDateTime created;
    List<RedirectUrlResponse> redirectUrls;

    // Error fields
    String message;
    String code;

    public boolean isError() {
        return isNotBlank(code);
    }
}
