package com.obaccelerator.portal.apiregistration;

import lombok.Value;

import java.util.List;

@Value
public class ApiRegistrationSteps {
    List<ApiRegistrationStep> stepResults;
    List<ApiRegistrationStepDefinition> stepDefinitions;
    ApiRegistrationStepDefinition currentStep;
}
