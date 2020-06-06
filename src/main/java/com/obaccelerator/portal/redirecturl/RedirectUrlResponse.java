package com.obaccelerator.portal.redirecturl;

import com.obaccelerator.common.rest.RestResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class RedirectUrlResponse extends RestResponse {

    private UUID id;
    private UUID organizationId;
    private String redirectUrl;
    private OffsetDateTime created;

}
