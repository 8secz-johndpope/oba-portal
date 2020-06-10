package com.obaccelerator.portal.bank;

import lombok.Value;

import java.util.List;

@Value
public class Bank {
    String bankSystemName;
    String displayName;
    List<BankGroup> groups;
    boolean beta;
    List<BankApi> bankApis;
}
