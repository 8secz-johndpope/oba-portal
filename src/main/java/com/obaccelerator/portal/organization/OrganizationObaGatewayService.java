package com.obaccelerator.portal.organization;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.portaluser.PortalUser;
import com.obaccelerator.portal.registration.Registration;
import com.obaccelerator.portal.token.TokenProviderService;
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
    private final TokenProviderService tokenProviderService;

    public OrganizationObaGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties,
                                         TokenProviderService tokenProviderService) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
        this.tokenProviderService = tokenProviderService;
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
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(new CreateOrganizationRequest(registration.getOrganizationName()));
    }

    ObaOrganizationResponse updateOrganization(UpdateObaOrganizationRequest updateObaOrganizationRequest, PortalUser portalUser) {

        RequestBuilder<UpdateObaOrganizationRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/organizations";
            HttpPut httpPut = new HttpPut(url);
            JsonHttpEntity<UpdateObaOrganizationRequest> entity = new JsonHttpEntity<>(input);
            httpPut.setEntity(entity);
            tokenProviderService.addOrganizationToken(httpPut, portalUser.getOrganizationId());
            return httpPut;
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganizationResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(updateObaOrganizationRequest);
    }

    ObaOrganizationResponse findOrganization(UUID id) {

        RequestBuilder<UUID> requestBuilder = input -> {
            String url = obaPortalProperties.getObaBaseUrl() +  "/organizations/" + id.toString();
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, id);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganizationResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(id);
    }
}

