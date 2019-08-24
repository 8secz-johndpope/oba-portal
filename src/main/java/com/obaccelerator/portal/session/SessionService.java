package com.obaccelerator.portal.session;

import com.obaccelerator.common.token.ApiTokenProcessingException;
import com.obaccelerator.common.token.TokenReader;
import com.obaccelerator.portal.cognito.CognitoService;
import com.obaccelerator.portal.cognito.PublicKeyEndpointInput;
import com.obaccelerator.portal.config.ObaPortalProperties;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class SessionService {

    private CognitoService cognitoService;
    private ObaPortalProperties obaPortalProperties;

    public SessionService(CognitoService cognitoService, ObaPortalProperties obaPortalProperties) {
        this.cognitoService = cognitoService;
        this.obaPortalProperties = obaPortalProperties;
    }

    public JsonWebKey fetchCognitoPublicKey(CognitoToken cognitoToken) {
        String cognitoJwtToken = cognitoToken.getAccessToken().getJwtToken();

        Optional<String> kidOptional;
        try {
            kidOptional = TokenReader.readHeaderWithoutSignatureVerification(cognitoJwtToken, "kid");
            if (!kidOptional.isPresent()) {
                throw new InvalidCognitoTokenException();
            }
        } catch (ApiTokenProcessingException e) {
            throw new InvalidCognitoTokenException();
        }

        CognitoKey cognitoKey = cognitoService.fetchCognitoPublicKey(new PublicKeyEndpointInput(obaPortalProperties.cognitoPublicKeysUrl()), kidOptional.get());

        JsonWebKey webKey = null;

        try {
            return new RsaJsonWebKey(cognitoKey.toMap());
        } catch (JoseException e) {
            throw new RuntimeException("Could not create web key from public key downloaded from AWS", e);
        }
    }

    public Map<String, Object> getTokenClaims(CognitoToken cognitoToken, JsonWebKey jsonWebKey) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256));
        try {
            jws.setCompactSerialization(cognitoToken.getAccessToken().getJwtToken());
            jws.setKey(jsonWebKey.getKey());
            boolean b = jws.verifySignature();
            if (!b) {
                throw new InvalidCognitoTokenException();
            }
        } catch (JoseException e) {
            throw new InvalidCognitoTokenException(e);
        }

        Map<String, Object> claims = null;
        try {
            claims = TokenReader.readClaims(cognitoToken.getAccessToken().getJwtToken(), jsonWebKey.getPublicKey());
        } catch (ApiTokenProcessingException e) {
            throw new InvalidCognitoTokenException();
        }

        long exp = (long) claims.get("exp");

        if (exp >= System.currentTimeMillis()) {
            throw new InvalidCognitoTokenException();
        }

        return claims;
    }
}
