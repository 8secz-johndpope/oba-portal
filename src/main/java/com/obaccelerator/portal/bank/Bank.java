package com.obaccelerator.portal.bank;

import com.obaccelerator.portal.api.Api;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Bank {
    String systemName;
    String displayName;
    String implementationKey;
    boolean beta;
    List<BankGroup> groups;
    List<Api> apis;
}
