package com.obaccelerator.portal.organization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class UpdateObaOrganizationRequest {

    @NotEmpty
    private String organizationId;
    @NotEmpty
    private String name;
    private String vatNumber;
    @NotEmpty
    private String street;
    @NotEmpty
    private String streetNumber;
    @NotEmpty
    private String postalCode;
    @NotEmpty
    private String country;
}
