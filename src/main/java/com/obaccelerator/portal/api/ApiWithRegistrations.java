package com.obaccelerator.portal.api;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ApiWithRegistrations {
    UUID id;
    boolean sandbox;
    ApiType type;
    List<String> countryDataProviders = new ArrayList<>();
    List<ApiRegistrationWithNumberOfConnections> apiRegistrations = new ArrayList<>();
    String title;
}
