package com.obaccelerator.portal.organization;

import com.obaccelerator.common.rest.RestResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ObaOrganizationResponse extends RestResponse {
    private UUID id;
    private String name;
    private String vatNumber;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String country;
    private OffsetDateTime created;
}
