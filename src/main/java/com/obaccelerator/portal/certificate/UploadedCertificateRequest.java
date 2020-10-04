package com.obaccelerator.portal.certificate;


import com.obaccelerator.common.model.certificate.KeyPurpose;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UploadedCertificateRequest {
    @NotEmpty
    private String privateKey;

    @NotEmpty
    private String certificate;

    @NotNull
    private KeyPurpose purpose;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
