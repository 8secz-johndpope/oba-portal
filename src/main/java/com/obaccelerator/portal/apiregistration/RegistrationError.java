package com.obaccelerator.portal.apiregistration;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class RegistrationError {
    protected abstract String getMessage();

    protected abstract String getCode();

    protected boolean isError() {
        return isNotBlank(getCode());
    }
}
