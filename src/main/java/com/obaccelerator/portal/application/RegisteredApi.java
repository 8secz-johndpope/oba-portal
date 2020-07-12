package com.obaccelerator.portal.application;


import com.obaccelerator.portal.api.GlobalApiStatus;
import lombok.Value;

import java.util.UUID;

@Value
public class RegisteredApi {
    UUID apiId;
    String financialOrganizationDisplayName;
    String financialOrganizationSystemName;
    String type;
    boolean sandbox;
    GlobalApiStatus globalStatus;
    boolean enabledForApplication;
}
