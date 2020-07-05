package com.obaccelerator.portal.api;

import com.obaccelerator.portal.financialorganization.FinancialOrganization;
import com.obaccelerator.portal.financialorganization.MainService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Api {
    UUID id;
    String type;
    String bankSystemName;
    String baseUrl;
    boolean requestSigningUsed;
    String requestSigningAlgorithm;
    boolean mutualTlsUsed;
    boolean sandbox;
    boolean beta;
    GlobalApiStatus globalStatus;
    List<MainService> mainServices;
    FinancialOrganization financialOrganization;
}

