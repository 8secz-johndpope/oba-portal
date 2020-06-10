package com.obaccelerator.portal.bank;

import lombok.Value;

import java.util.List;

@Value
public class MainService {
    String systemName;
    String displayName;
    List<SubService> subServices;
}
