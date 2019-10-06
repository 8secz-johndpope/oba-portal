package com.obaccelerator.portal.portaluser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PortalUser {
    private UUID id;
    private String cognitoUserId;
    private UUID organizationId;
    private OffsetDateTime firstLogin;
    private OffsetDateTime created;
}

