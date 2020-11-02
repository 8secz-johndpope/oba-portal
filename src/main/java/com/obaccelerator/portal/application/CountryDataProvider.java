package com.obaccelerator.portal.application;

import com.obaccelerator.portal.api.ApiType;
import com.obaccelerator.portal.api.Status;
import lombok.Value;

@Value
public class CountryDataProvider {
    String systemName;
    String displayName;
    Country country;
    String timeZone;
    Currency currency;
    ApiType type;
    Status status;
    boolean sandbox;
    Logos logos;
    DescriptionField bankPreferredDescriptionField;
}
