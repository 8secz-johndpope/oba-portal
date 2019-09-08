package com.obaccelerator.portal.session;

import lombok.Value;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class Session {

    private static final int SESSION_MINUTES = 30;

    private UUID session;
    private OffsetDateTime lastUsed;
    private OffsetDateTime created;

    // TODO: security : move this logic to SQL
    public boolean isExpired() {
        return Duration.between(created, lastUsed).toMinutes() >= SESSION_MINUTES;
    }
}
