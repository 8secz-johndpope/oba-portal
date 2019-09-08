package com.obaccelerator.portal.gateway.organization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obaccelerator.common.endpoint.EndpointDef;
import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestBuilder;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.session.ObaOrganization;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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

        RequestBuilder<CreateOrganisationRequest> requestBuilder = input -> {
            ObjectMapper mapper = new ObjectMapper();
            String url = obaPortalProperties.getObaBaseUrl() + EndpointDef.Path.POST_ORGANIZATIONS;
            HttpPost httpPost = new HttpPost(url);

            String createOrganisationRequest;
            try {
                createOrganisationRequest = mapper.writer().writeValueAsString(input);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            HttpEntity stringEntity = new StringEntity(createOrganisationRequest, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            return httpPost;
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, ObaOrganization.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logResponses(true)
                .build()
                .execute(new CreateOrganisationRequest(uuid));
    }

}
