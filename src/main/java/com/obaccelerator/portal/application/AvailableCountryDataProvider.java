package com.obaccelerator.portal.application;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailableCountryDataProvider {
    CountryDataProvider countryDataProvider;
    boolean enabled;
}
