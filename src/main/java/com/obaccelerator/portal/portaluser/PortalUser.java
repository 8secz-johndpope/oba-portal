package com.obaccelerator.portal.portaluser;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Value
public class PortalUser {
    UUID id;
    String cognitoUserId;
    UUID organizationId;
    OffsetDateTime firstLogin;
    OffsetDateTime created;
    List<String> roles;

    public boolean belongsToOrganization(String organizationId) {
        return UUID.fromString(organizationId).equals(this.organizationId);
    }
}

