package com.obaccelerator.portal.certificate;

import com.obaccelerator.common.http.*;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            String url = obaPortalProperties.getObaBaseUrl() + "/generated-certificates";
            HttpPost httpPost = new HttpPost(url);
            JsonHttpEntity<CreateOrganizationCertificateRequest> entity = new JsonHttpEntity<>(input);
            httpPost.setEntity(entity);
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(certificateRequest);
    }

    public CertificateResponse uploadCertificatesToOba(UploadedCertificateRequest certificateRequest, UUID organizationId) {

        RequestBuilder<UploadedCertificateRequest> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/uploaded-certificates";
            HttpPost httpPost = new HttpPost(url);
            JsonHttpEntity<UploadedCertificateRequest> entity = new JsonHttpEntity<>(input);
            httpPost.setEntity(entity);
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(201))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(certificateRequest);
    }

    public CertificateListResponse findAllForOrganization(UUID organizationId, boolean nonExpiredOnly) {

        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/certificates";
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        CertificateListResponse resp = new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, CertificateListResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(organizationId);

        if (nonExpiredOnly) {
            List<CertificateResponse> collect = resp.stream().filter(c -> OffsetDateTime.now().isBefore(c.getValidUntil())).collect(Collectors.toList());
            CertificateListResponse response = new CertificateListResponse();
            response.addAll(collect);
            return response;
        }

        return resp;
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
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(organizationId);
    }
}
