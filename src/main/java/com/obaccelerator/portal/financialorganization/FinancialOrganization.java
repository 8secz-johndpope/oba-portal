package com.obaccelerator.portal.financialorganization;

import com.obaccelerator.portal.api.Api;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FinancialOrganization {
    String systemName;
    String displayName;
    String implementationKey;
    List<BankGroup> groups;
    List<Api> apis;
}
