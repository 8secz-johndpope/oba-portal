package com.obaccelerator.portal.api;

import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestBuilder;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.portal.apiregistration.ByOrganizationAndApi;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

@Service
public class ApiGatewayService {

    private final ObaPortalProperties obaPortalProperties;
    private final TokenProviderService tokenProviderService;
    private final HttpClient httpClient;

    public ApiGatewayService(ObaPortalProperties obaPortalProperties, TokenProviderService tokenProviderService,
                             HttpClient httpClient) {
        this.obaPortalProperties = obaPortalProperties;
        this.tokenProviderService = tokenProviderService;
        this.httpClient = httpClient;
    }

    public Api findApi(ByOrganizationAndApi byOrganizationAndApi) {
        RequestBuilder<ByOrganizationAndApi> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/apis/" + input.getApiId();
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, byOrganizationAndApi.getOrganizationId());
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, Api.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(byOrganizationAndApi);
    }
}
