package com.obaccelerator.portal.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CognitoIdToken {

    @NotEmpty
    private String jwtToken;
    @Valid
    private PayLoad payload;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessTokenContent {
        private String jwtToken;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayLoad {
        @Email
        private String email;
        @JsonProperty("email_verified")
        private boolean emailVerified;
        @NotEmpty
        @JsonProperty("family_name")
        private String familyName;
        @NotEmpty
        @JsonProperty("given_name")
        private String givenName;
        private long iat;
        @NotEmpty
        private String sub; // user id
        @NotEmpty
        private String aud;

    }
}
