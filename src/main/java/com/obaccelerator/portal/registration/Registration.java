package com.obaccelerator.portal.registration;

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
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private OffsetDateTime created;
}
