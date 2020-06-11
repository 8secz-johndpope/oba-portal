package com.obaccelerator.portal.bank;

import lombok.Value;

import java.util.List;

@Value
public class Api {
    List<MainService> mainServices;
    String type;
    String baseUrl;
    boolean requestSigningUsed;
    boolean requestSigningAlgorithm;
    boolean mutualTlsUsed;
    boolean sandbox;
    BankRegistration bankRegistration;
}
