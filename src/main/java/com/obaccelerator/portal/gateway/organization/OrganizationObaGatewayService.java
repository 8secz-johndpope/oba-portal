package com.obaccelerator.portal.gateway.organization;

import com.obaccelerator.common.endpoint.EndpointDef;
import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.session.ObaOrganization;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationObaGatewayService {

    private HttpClient obaHttpClient;
    private ObaPortalProperties obaPortalProperties;

    public OrganizationObaGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
    }

    public ObaOrganization createOrganization(UUID uuid) {

        RequestBuilder<UUID> requestBuilder = input -> {
            String url = obaPortalProperties.getObaBaseUrl() + EndpointDef.Path.POST_ORGANIZATIONS;
            HttpPost httpPost = new HttpPost(url);
            JsonHttpEntity<CreateOrganizationRequest> entity = new JsonHttpEntity<>(new CreateOrganizationRequest(input));
            httpPost.setEntity(entity);
            return httpPost;
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganization.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(uuid);
    }

    public ObaOrganization findOrganization(UUID id) {

        RequestBuilder<UUID> requestBuilder = input -> {
            String url = obaPortalProperties.getObaBaseUrl() + EndpointDef.Path.GET_ORGANIZATIONS;
            return new HttpGet(url);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganization.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(id);
    }
}
