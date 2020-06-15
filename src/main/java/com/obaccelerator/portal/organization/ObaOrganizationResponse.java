package com.obaccelerator.portal.organization;

import com.obaccelerator.common.rest.RestResponse;
import com.obaccelerator.portal.redirecturl.RedirectUrlResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@Setter
public class ObaOrganizationResponse extends RestResponse {
    private UUID id;
    private String name;
    private String vatNumber;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String country;
    private List<RedirectUrlResponse> redirectUrls;
    private OffsetDateTime created;

    public boolean isComplete() {
        return !isBlank(name) && !isBlank(street) && !isBlank(streetNumber) && !isBlank(postalCode) && !isBlank(country);
    }
}
