package com.obaccelerator.portal.organization;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ObaOrganization {
    private UUID id;
    private String name;
    private String vatNumber;
    private String street;
    private String streetNumber;
    private OffsetDateTime created;
}
