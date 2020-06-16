package com.obaccelerator.portal.apiregistration;

import lombok.Value;

import java.util.UUID;

@Value
public class ByOrganizationAndApi {
    UUID organizationId;
    UUID apiId;
}
