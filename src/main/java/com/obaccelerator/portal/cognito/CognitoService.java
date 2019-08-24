package com.obaccelerator.portal.cognito;

import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.portal.session.CognitoKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CognitoService {

    private PublicKeyRequestBuilder publicKeyRequestBuilder;
    private HttpClient httpClient;

    public CognitoService(PublicKeyRequestBuilder publicKeyRequestBuilder, HttpClient httpClient) {
        this.publicKeyRequestBuilder = publicKeyRequestBuilder;
        this.httpClient = httpClient;
    }

    public CognitoKey fetchCognitoPublicKey(PublicKeyEndpointInput input, String keyId) {

        RequestExecutor<PublicKeyEndpointInput, PublicKeyCollection> executor = new RequestExecutor<>(
                publicKeyRequestBuilder,
                httpClient,
                PublicKeyCollection.class,
                new ExpectedHttpCodesValidator(200),
                new ResponseNotEmptyValidator());

        PublicKeyCollection publicKeyCollection = executor.execute(input);
        return publicKeyCollection.getKey(keyId);
    }

}
