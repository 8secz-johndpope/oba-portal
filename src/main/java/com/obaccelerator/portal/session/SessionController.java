package com.obaccelerator.portal.session;

import com.obaccelerator.common.type.Customer;
import com.obaccelerator.portal.gateway.customer.CustomerGatewayService;
import org.jose4j.jwk.JsonWebKey;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SessionController {

    private SessionService sessionService;
    private CustomerGatewayService customerGatewayService;

    public SessionController(SessionService sessionService, CustomerGatewayService customerGatewayService) {
        this.sessionService = sessionService;
        this.customerGatewayService = customerGatewayService;
    }

    @PostMapping("/sessions")
    public String cognitoTokenToSession(@RequestBody CognitoToken cognitoToken) {

        JsonWebKey jsonWebKey = sessionService.fetchCognitoPublicKey(cognitoToken);
        Map<String, Object> tokenClaims = sessionService.getTokenClaims(cognitoToken, jsonWebKey);

        // Create customer
        //customerService.createCustomer()

        Customer customer = customerGatewayService.createCustomer("", "");


        // Create user without api_client reference
        // Create a session

        return "";
    }

}
