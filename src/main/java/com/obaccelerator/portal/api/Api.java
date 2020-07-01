package com.obaccelerator.portal.api;

import com.obaccelerator.portal.bank.FinancialOrganization;
import com.obaccelerator.portal.bank.MainService;
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
    List<MainService> mainServices;
    FinancialOrganization financialOrganization;
}

