package com.obaccelerator.portal.gateway.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obaccelerator.common.endpoint.EndpointDef;
import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestBuilder;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.common.type.Customer;
import com.obaccelerator.portal.config.ObaPortalProperties;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerGatewayService {

    private HttpClient obaHttpClient;
    private ObaPortalProperties obaPortalProperties;

    public CustomerGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
    }

    public Customer createCustomer(String name, String email) {

        RequestBuilder<CreateCustomerRequestInput> requestBuilder = input -> {
            ObjectMapper mapper = new ObjectMapper();
            HttpPost httpPost = new HttpPost(obaPortalProperties.getObaBaseUrl() + EndpointDef.POST_CUSTOMERS);

            String createCustomerRequest;
            try {
                createCustomerRequest = mapper.writer().writeValueAsString(input);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            HttpEntity stringEntity = new StringEntity(createCustomerRequest, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            return httpPost;
        };

        RequestExecutor<CreateCustomerRequestInput, Customer> executor = new RequestExecutor<>(
                requestBuilder,
                obaHttpClient,
                Customer.class,
                new ResponseNotEmptyValidator(), new ExpectedHttpCodesValidator(201));

        return executor.execute(new CreateCustomerRequestInput(name, email));
    }
}
