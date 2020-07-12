package com.obaccelerator.portal.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
public class CountryDataProvider {
    String systemName;
    String displayName;
    String country;
    String timeZone;
    Currency currency;
}
