package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.model.certificate.KeyPurpose;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateOrganizationCertificateRequest {

    @NotEmpty
    String name;

    String description;
    KeyPurpose purpose;

    @NotEmpty
    String country;
    @NotEmpty
    String stateOrProvince;
    @NotEmpty
    String locality;
    @NotEmpty
    String organizationName;
    @NotEmpty
    String organizationalUnitName;
    @NotEmpty
    String commonName;
    @Email
    String emailAddress;

    private boolean roleAisp;
    private boolean rolePisp;
    private boolean roleCisp;
}