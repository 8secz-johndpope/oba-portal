package com.obaccelerator.portal.auth.cognito;

import com.obaccelerator.common.http.RequestBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.stereotype.Component;

@Component
public class PublicKeyRequestBuilder implements RequestBuilder<PublicKeyEndpointInput> {

    @Override
    public HttpUriRequest build(PublicKeyEndpointInput input) {
        return new HttpGet(input.getUrl());
    }
}
