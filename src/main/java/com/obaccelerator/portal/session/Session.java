package com.obaccelerator.portal.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * The OBA session. Don't return the sessionId in this object, it is not secure.
 */
@Value
public class Session {
    private static final int SESSION_MINUTES = 30;

    /**
     * Do not return the session id to the front-end in a JSON response. Only in a cookie.
     */
    @JsonIgnore
    private UUID id;


    private UUID organizationId;
    private OffsetDateTime lastUsed;
    private OffsetDateTime created;


}
