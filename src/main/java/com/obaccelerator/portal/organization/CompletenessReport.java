package com.obaccelerator.portal.organization;

import lombok.Value;

@Value
public class CompletenessReport {
    boolean redirectUrl;
    boolean organizationFieldsComplete;
    boolean validSigningCertificate;
    boolean validTransportCertificate;
}
