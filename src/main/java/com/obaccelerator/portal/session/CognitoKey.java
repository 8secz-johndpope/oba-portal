package com.obaccelerator.portal.session;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CognitoKey {
    private String alg;
    private String e;
    private String kid;
    private String kty;
    private String n;
    private String use;

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("alg", alg);
        map.put("e", e);
        map.put("kid", kid);
        map.put("kty", kty);
        map.put("n", n);
        map.put("use", use);
        return map;
    }
}
