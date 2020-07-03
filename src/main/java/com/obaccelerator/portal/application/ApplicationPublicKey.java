package com.obaccelerator.portal.application;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ApplicationPublicKey {
    UUID id;
    UUID applicationId;
    UUID kid;
    String publicKey;
    OffsetDateTime created;
}
