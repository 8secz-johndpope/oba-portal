package com.obaccelerator.portal.redirecturl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedirectUrlWithNumberOfRegistrations {
    RedirectUrlResponse redirectUrl;
    int usedInNumberOfRegistrations;
}
