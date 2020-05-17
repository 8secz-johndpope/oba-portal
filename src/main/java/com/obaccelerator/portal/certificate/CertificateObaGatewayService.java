package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
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

    public CertificateObaGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
    }

    public CertificateResponse createCertificateInOba(CreateOrganizationCertificateRequest certificateRequest, String organizationId) {

        RequestBuilder<CreateOrganizationCertificateRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/" + organizationId + "/certificates";
            HttpPost httpPost = new HttpPost(url);
            JsonHttpEntity<CreateOrganizationCertificateRequest> entity = new JsonHttpEntity<>(input);
            httpPost.setEntity(entity);
            return httpPost;
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(certificateRequest);
    }

    public CertificateListResponse findAll(UUID organizationId) {

        RequestBuilder<UUID> requestBuilder = (input) -> new HttpGet(obaPortalProperties.getObaBaseUrl() + "/" + organizationId + "/certificates");

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateListResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(organizationId);
    }
}
