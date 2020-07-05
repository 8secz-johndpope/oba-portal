package com.obaccelerator.portal.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GlobalApiStatus {

    public enum Status {
        BETA,
        OPEN,
        CLOSED
    }
    Status status;
    OffsetDateTime closedFrom;
    OffsetDateTime closedUntil;
}
