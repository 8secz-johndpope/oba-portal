package com.obaccelerator.portal.apiregistration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.obaccelerator.portal.apiregistration.form.FormDefinition;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRegistrationStepDefinition {

    public Integer stepNr;
    public String apiId;
    public List<OrganizationCertificate> signingCertificates = null;
    public List<OrganizationCertificate> transportCertificates = null;
    public FormDefinition formDefinition;
}
