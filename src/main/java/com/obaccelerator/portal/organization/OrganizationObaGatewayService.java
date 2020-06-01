package com.obaccelerator.portal.organization;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.registration.Registration;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationObaGatewayService {

    private final HttpClient obaHttpClient;
    private final ObaPortalProperties obaPortalProperties;

    public OrganizationObaGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
    }

    public ObaOrganizationResponse createOrganizationFromRegistration(Registration registration) {

        RequestBuilder<CreateOrganizationRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/organizations";
            HttpPost httpPost = new HttpPost(url);
            JsonHttpEntity<CreateOrganizationRequest> entity = new JsonHttpEntity<>(input);
            httpPost.setEntity(entity);
            return httpPost;
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganizationResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(new CreateOrganizationRequest(registration.getOrganizationName()));
    }

    ObaOrganizationResponse updateOrganization(UpdateObaOrganizationRequest updateObaOrganizationRequest) {

        RequestBuilder<UpdateObaOrganizationRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/organizations";
            HttpPut httpPut = new HttpPut(url);
            JsonHttpEntity<UpdateObaOrganizationRequest> entity = new JsonHttpEntity<>(input);
            httpPut.setEntity(entity);
            return httpPut;
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganizationResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(updateObaOrganizationRequest);
    }

    ObaOrganizationResponse findOrganization(UUID id) {

        RequestBuilder<UUID> requestBuilder = input -> {
            String url = obaPortalProperties.getObaBaseUrl() +  "/organizations/" + id.toString();
            return new HttpGet(url);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganizationResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(id);
    }
}

