package com.obaccelerator.portal.session;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ObaOrganization {
    private UUID id;
    private OffsetDateTime created;
}
