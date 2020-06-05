package com.obaccelerator.portal.redirecturl;

import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestBuilder;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.portal.certificate.CertificateListResponse;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

    public List<RedirectUrl> findAllForOrganization(UUID organizationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            HttpGet httpGet = new HttpGet(obaPortalProperties.getObaBaseUrl() + "/" + organizationId + "/redirect-urls");
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, RedirectUrlListResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsResponsesOnErrorForOrganizations())
                .build()
                .execute(organizationId);
    }
}
