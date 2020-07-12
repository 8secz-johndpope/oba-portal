package com.obaccelerator.portal.api;

import com.obaccelerator.portal.apiregistration.ApiRegistration;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ApiWithCountryDataProviders {
    UUID apiId;
    ApiType type;
    FlowType flowType;
    boolean sandbox;
    Status status;
    List<String> services = new ArrayList<>();
    List<ApiRegistration> apiRegistrations;
    List<CountryDataProvider> countryDataProviders;
    String title;
}
