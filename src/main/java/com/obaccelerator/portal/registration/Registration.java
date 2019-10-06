package com.obaccelerator.portal.registration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    private UUID id;
    private UUID cognitoUserId;
    private String organizationName;
    private UUID promotedToOrganization;
    private OffsetDateTime created;

    @JsonIgnore
    public boolean isPromotedToOrganizationWithId() {
        return promotedToOrganization != null;
    }
}
