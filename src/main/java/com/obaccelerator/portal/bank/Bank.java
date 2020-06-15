package com.obaccelerator.portal.bank;

import lombok.Value;

import java.util.List;

@Value
public class Bank {
    String bankSystemName;
    String displayName;
    String implementationKey;
    boolean beta;
    List<BankGroup> groups;
    List<Api> apis;
}
