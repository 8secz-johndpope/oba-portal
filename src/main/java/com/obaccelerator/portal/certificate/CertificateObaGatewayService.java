package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CertificateObaGatewayService {

    private final HttpClient obaHttpClient;
    private final ObaPortalProperties obaPortalProperties;
    private TokenProviderService tokenProviderService;

    public CertificateObaGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties,
                                        TokenProviderService tokenProviderService) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
        this.tokenProviderService = tokenProviderService;
    }

    public CertificateResponse createCertificateInOba(CreateOrganizationCertificateRequest certificateRequest, UUID organizationId) {

        RequestBuilder<CreateOrganizationCertificateRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/certificates";
            HttpPost httpPost = new HttpPost(url);
            JsonHttpEntity<CreateOrganizationCertificateRequest> entity = new JsonHttpEntity<>(input);
            httpPost.setEntity(entity);
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(certificateRequest);
    }

    public CertificateListResponse findAllForOrganization(UUID organizationId) {

        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/certificates";
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateListResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(organizationId);
    }

    public CertificateResponse findOneForOrganization(UUID organizationId, UUID certificateId) {

        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/certificates/" + certificateId;
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(organizationId);
    }
}
