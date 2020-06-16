package com.obaccelerator.portal.apiregistration;

import lombok.Value;

import java.util.List;

@Value
public class ApiRegistrationSteps {
    List<ApiRegistrationStepResult> stepResults;
    List<ApiRegistrationStepDefinition> stepDefinitions;
}
