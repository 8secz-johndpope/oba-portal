package com.obaccelerator.portal.application;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ApplicationPublicKey {
    UUID id;
    UUID applicationId;
    UUID kid;
    String publicKey;
    OffsetDateTime created;
}
