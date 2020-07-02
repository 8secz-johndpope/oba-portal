package com.obaccelerator.portal.application;

import com.obaccelerator.common.text.ObaRegex;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ContactPerson {

    @Email
    String email;
    @Pattern(regexp = ObaRegex.PATTERN_NAME)
    String name;
}
