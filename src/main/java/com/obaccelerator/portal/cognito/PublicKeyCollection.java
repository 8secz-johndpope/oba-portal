package com.obaccelerator.portal.cognito;

import com.obaccelerator.portal.session.CognitoKey;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PublicKeyCollection {

    private List<CognitoKey> keys = new ArrayList<>();



    public CognitoKey getKey(String keyId) {
        for (CognitoKey key : keys) {
            if(key.getKid().equals(keyId)) {
                return key;
            }
        }
        throw new IllegalArgumentException("Key with id " + keyId + " was not found");
    }
}


