package com.obaccelerator.portal.apiregistration;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubmittedValue {
    private String key;
    private List<String> values;
}
