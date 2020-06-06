package com.obaccelerator.portal.redirecturl;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RedirectUrlGatewayService {

    private final HttpClient obaHttpClient;
    private final ObaPortalProperties obaPortalProperties;
    private final TokenProviderService tokenProviderService;

    public RedirectUrlGatewayService(ObaPortalProperties obaPortalProperties,
                                     TokenProviderService tokenProviderService,
                                     HttpClient obaHttpClient) {
        this.obaPortalProperties = obaPortalProperties;
        this.tokenProviderService = tokenProviderService;
        this.obaHttpClient = obaHttpClient;
    }

    public List<RedirectUrlResponse> findAllForOrganization(UUID organizationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            HttpGet httpGet = new HttpGet(obaPortalProperties.getObaBaseUrl() + "/redirect-urls");
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, RedirectUrlListResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(organizationId);
    }

    public RedirectUrlResponse create(UUID organizationId, CreateRedirectUrlRequest redirectUrlRequest) {
        RequestBuilder<CreateRedirectUrlRequest> requestBuilder = (input) -> {
            HttpPost httpPost = new HttpPost(obaPortalProperties.getObaBaseUrl() + "/redirect-urls");
            JsonHttpEntity<CreateRedirectUrlRequest> entity = new JsonHttpEntity<>(input);
            httpPost.setEntity(entity);
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, RedirectUrlResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(redirectUrlRequest);
    }

    public void delete(UUID organizationId, UUID redirectUrlId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            HttpDelete httpDelete = new HttpDelete(obaPortalProperties.getObaBaseUrl() + "/redirect-urls/" + input.toString());
            return tokenProviderService.addOrganizationToken(httpDelete, organizationId);
        };

        new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, Void.class)
                .addResponseValidator(new ExpectedHttpCodesValidator(204))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(redirectUrlId);
    }
}
