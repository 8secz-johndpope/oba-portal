package com.obaccelerator.portal.apiregistration;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubmittedForm {
    private List<SubmittedValue> values;
}
