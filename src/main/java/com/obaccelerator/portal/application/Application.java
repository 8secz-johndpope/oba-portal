package com.obaccelerator.portal.application;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class Application {
    UUID id;
    UUID organizationId;
    String name;
    int numberOfAutomatedRefreshesPerDay;
    String technicalContactName;
    String technicalContactEmail;
    OffsetDateTime created;
}
