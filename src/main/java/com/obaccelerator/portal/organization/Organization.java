package com.obaccelerator.portal.organization;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class Organization {
    private UUID id;
    private String name;
    private String vatNumber;
    private String street;
    private String streetNumber;
    private OffsetDateTime created;
}
