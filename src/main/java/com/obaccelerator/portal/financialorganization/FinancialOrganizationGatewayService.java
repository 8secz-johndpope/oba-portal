package com.obaccelerator.portal.financialorganization;

import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestBuilder;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FinancialOrganizationGatewayService {

    private final HttpClient obaHttpClient;
    private final ObaPortalProperties obaPortalProperties;
    private final TokenProviderService tokenProviderService;

    public FinancialOrganizationGatewayService(HttpClient obaHttpClient, ObaPortalProperties obaPortalProperties, TokenProviderService tokenProviderService) {
        this.obaHttpClient = obaHttpClient;
        this.obaPortalProperties = obaPortalProperties;
        this.tokenProviderService = tokenProviderService;
    }

    public FOListResponse findFinancialOrganizations(UUID organizationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/financial-organizations/";
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, FOListResponse.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(organizationId);
    }

    public FinancialOrganization findFinancialOrganization(UUID organizationId, String bankSystemName) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/financial-organizations/" + bankSystemName;
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, obaHttpClient, FinancialOrganization.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(organizationId);
    }
}
