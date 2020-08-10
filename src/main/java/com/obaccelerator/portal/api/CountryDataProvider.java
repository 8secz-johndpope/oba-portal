package com.obaccelerator.portal.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryDataProvider {
    String systemName;
    String displayName;
    String country;
    String timeZone;
    Currency currency;
}
