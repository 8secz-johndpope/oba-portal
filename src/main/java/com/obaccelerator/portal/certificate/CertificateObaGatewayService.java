package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CertificateObaGatewayService {

    private final HttpClient obaHttpClient;
    private final ObaPortalProperties obaPortalProperties;

    public CertificateObaGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
    }

    public CreateCertificateResponse createCertificateInOba(CreateOrganizationCertificateRequest certificateRequest, String organizationId) {

        RequestBuilder<CreateOrganizationCertificateRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/" + organizationId + "/certificates";
            HttpPost httpPost = new HttpPost(url);
            JsonHttpEntity<CreateOrganizationCertificateRequest> entity = new JsonHttpEntity<>(input);
            httpPost.setEntity(entity);
            return httpPost;
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CreateCertificateResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(certificateRequest);
    }
}
