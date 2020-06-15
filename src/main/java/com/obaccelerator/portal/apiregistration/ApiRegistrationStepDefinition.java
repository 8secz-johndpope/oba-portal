package com.obaccelerator.portal.apiregistration;

import com.obaccelerator.common.form.FormDefinition;
import lombok.Value;

import java.util.List;
import java.util.UUID;


@Value
public class ApiRegistrationStepDefinition {
    int stepNr;
    UUID apiId;
    List<OrganizationCertificate> signingCertificates;
    List<OrganizationCertificate> transportCertificates;
    FormDefinition formDefinition;
}
