package com.obaccelerator.portal.application;

import com.obaccelerator.common.text.ObaRegex;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class CreateApplicationRequest {
    @Pattern(regexp = ObaRegex.PATTERN_NAME)
    private String name;
    @Email
    String technicalContactEmail;
    @Pattern(regexp = ObaRegex.PATTERN_NAME)
    String technicalContactName;
}
