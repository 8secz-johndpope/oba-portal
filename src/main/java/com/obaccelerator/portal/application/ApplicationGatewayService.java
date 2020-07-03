package com.obaccelerator.portal.application;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.apiregistration.ApiRegistrationList;
import com.obaccelerator.portal.apiregistration.ByOrganizationAndApi;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApplicationGatewayService {

    private final ObaPortalProperties obaPortalProperties;
    private final TokenProviderService tokenProviderService;
    private HttpClient httpClient;

    public ApplicationGatewayService(ObaPortalProperties obaPortalProperties,
                                     TokenProviderService tokenProviderService,
                                     HttpClient httpClient) {
        this.obaPortalProperties = obaPortalProperties;
        this.tokenProviderService = tokenProviderService;
        this.httpClient = httpClient;
    }

    public List<Application> findApplications(UUID organizationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/applications";
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApplicationList.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(organizationId);
    }

    public Application findApplication(UUID organizationId, UUID applicationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/applications/" + applicationId.toString();
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, Application.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(applicationId);
    }

    public Application createApplication(UUID organizationId, CreateApplicationRequest request) {
        RequestBuilder<CreateApplicationRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/applications";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new JsonHttpEntity<>(request));
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, Application.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(request);
    }

    public void deleteApplication(UUID organizationId, UUID applicationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/applications/" + applicationId.toString();
            HttpDelete httpDelete = new HttpDelete(url);
            return tokenProviderService.addOrganizationToken(httpDelete, organizationId);
        };

        new RequestExecutor.Builder<>(requestBuilder, httpClient, Void.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(applicationId);
    }

    public List<ApplicationPublicKey> findApplicationPublicKeys(UUID organizationId, UUID applicationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/applications/" + applicationId.toString() + "/public-keys";
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApplicationPublicKeyList.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(applicationId);
    }

    public ApplicationPublicKey createApplicationPublicKey(UUID organizationId, UUID applicationId, CreateApplicationPublicKeyRequest request) {
        RequestBuilder<CreateApplicationPublicKeyRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/applications/" + applicationId.toString() + "/public-keys";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new JsonHttpEntity<>(request));
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApplicationPublicKey.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(request);
    }

    public void deleteApplicationPublicKey(UUID organizationId, UUID applicationId, UUID publicKeyId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/applications/" + applicationId.toString() + "/public-keys/" + publicKeyId.toString();
            HttpDelete httpDelete = new HttpDelete(url);
            return tokenProviderService.addOrganizationToken(httpDelete, organizationId);
        };

        new RequestExecutor.Builder<>(requestBuilder, httpClient, Void.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(publicKeyId);
    }
}
