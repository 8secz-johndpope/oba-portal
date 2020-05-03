package com.obaccelerator.portal.auth.cognito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.common.token.ApiTokenProcessingException;
import com.obaccelerator.common.token.TokenReader;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.session.CognitoIdToken;
import com.obaccelerator.portal.session.CognitoKey;
import com.obaccelerator.portal.session.InvalidCognitoTokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CognitoService {

    private PublicKeyRequestBuilder publicKeyRequestBuilder;
    private ObaPortalProperties portalProperties;
    private HttpClient httpClient;

    public CognitoService(PublicKeyRequestBuilder publicKeyRequestBuilder, ObaPortalProperties portalProperties, HttpClient httpClient) {
        this.publicKeyRequestBuilder = publicKeyRequestBuilder;
        this.portalProperties = portalProperties;
        this.httpClient = httpClient;
    }

    public CognitoKey fetchCognitoPublicKey(PublicKeyEndpointInput input, String keyId) {
        PublicKeyCollection publicKeyCollection = new RequestExecutor.Builder<>(publicKeyRequestBuilder, httpClient, PublicKeyCollection.class)
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .addResponseValidator(new ResponseNotEmptyValidator())
                .build()
                .execute(input);

        return publicKeyCollection.getKey(keyId);
    }

    public Map<String, Object> verifyAndGetCognitoClaimsFromRequest(HttpServletRequest request) {
        CognitoIdToken cognitoToken = null;
        try {
            cognitoToken = new ObjectMapper().readValue(request.getInputStream(), CognitoIdToken.class);
            return verifyAndGetCognitoClaims(cognitoToken);
        } catch (IOException e) {
            throw new RuntimeException("Could not get Cognito token from response body");
        }
    }

    private Map<String, Object> verifyAndGetCognitoClaims(CognitoIdToken cognitoToken) {

        JsonWebKey jsonWebKey = fetchCognitoPublicKey(cognitoToken);

        Map<String, Object> claims = null;
        try {
            claims = TokenReader.readClaims(cognitoToken.getJwtToken(), cognitoToken.getPayload().getAud(), jsonWebKey.getPublicKey());
        } catch (ApiTokenProcessingException e) {
            throw new InvalidCognitoTokenException(e);
        }

        long exp = (long) claims.get("exp");

        if (exp >= System.currentTimeMillis()) {
            throw new InvalidCognitoTokenException("Cognito token expired");
        }

        return claims;
    }

    private JsonWebKey fetchCognitoPublicKey(CognitoIdToken cognitoToken) {
        String cognitoJwtToken = cognitoToken.getJwtToken();

        Optional<String> kidOptional;
        try {
            kidOptional = TokenReader.readHeaderWithoutSignatureVerification(cognitoJwtToken, "kid");
            if (!kidOptional.isPresent()) {
                throw new InvalidCognitoTokenException("No kid found in Cognito token");
            }
        } catch (ApiTokenProcessingException e) {
            throw new InvalidCognitoTokenException(e);
        }

        CognitoKey cognitoKey = fetchCognitoPublicKey(new PublicKeyEndpointInput(portalProperties.cognitoPublicKeysUrl()), kidOptional.get());

        JsonWebKey webKey = null;

        try {
            return new RsaJsonWebKey(cognitoKey.toMap());
        } catch (JoseException e) {
            throw new RuntimeException("Could not create web key from public key downloaded from AWS", e);
        }
    }

}
