package com.obaccelerator.portal.applications;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class Application {
    UUID id;
    String name;
    int nrOfAutomatedRefreshesPerDay;
    String technicalContactName;
    String technicalContactEmail;
    OffsetDateTime created;
}
