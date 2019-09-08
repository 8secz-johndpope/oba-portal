package com.obaccelerator.portal.registration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@Setter
public class RegistrationRequest {

    @Pattern(regexp = "^([A-Za-z '-]{1,30})")
    private String firstName;
    @Pattern(regexp = "^([A-Za-z '-]{1,30})")
    private String lastName;
    @Pattern(regexp = "^([A-Za-z0-9 '-?!_&*]{1,50})")
    private String organizationName;
    @Email
    @JsonProperty("dqwuh")
    private String email; // this is the actual email address
    @NotEmpty
    private String cognitoUserId;

    /**
     * Honey pot fields - if any of these are filled we are likely dealing with a bot
     */
    @JsonProperty("companyLocation")
    private String honeyPotCompanyLocation;
    @JsonProperty("email")
    private String honeyPotEmail;
    @JsonProperty("name")
    private String honeyPotName;

    @JsonIgnore
    public boolean isLikelyABotRequest() {
        return isNotBlank(honeyPotCompanyLocation) || isNotBlank(honeyPotEmail) || isNotBlank(honeyPotName);
    }
}
