package com.obaccelerator.portal.api;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class ApiRegistrationWithNumberOfConnections {
    UUID id;
    int nrOfConnections;
    OffsetDateTime created;
    boolean enabled;

}

