package com.obaccelerator.portal.organization;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class Organization {
    UUID id;
    String name;
    String vatNumber;
    String street;
    String streetNumber;
    OffsetDateTime created;
}
