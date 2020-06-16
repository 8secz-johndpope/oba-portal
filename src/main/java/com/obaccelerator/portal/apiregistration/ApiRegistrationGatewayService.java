package com.obaccelerator.portal.apiregistration;

import com.obaccelerator.common.http.ExpectedHttpCodesValidator;
import com.obaccelerator.common.http.RequestBuilder;
import com.obaccelerator.common.http.RequestExecutor;
import com.obaccelerator.common.http.ResponseNotEmptyValidator;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApiRegistrationGatewayService {

    private final ObaPortalProperties obaPortalProperties;
    private final TokenProviderService tokenProviderService;
    private final HttpClient httpClient;

    public ApiRegistrationGatewayService(ObaPortalProperties obaPortalProperties,
                                         TokenProviderService tokenProviderService,
                                         HttpClient httpClient) {
        this.obaPortalProperties = obaPortalProperties;
        this.tokenProviderService = tokenProviderService;
        this.httpClient = httpClient;
    }

    public List<ApiRegistration> findApiRegistrations(ByOrganizationAndApi byOrganizationAndApi) {
        RequestBuilder<ByOrganizationAndApi> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registrations/" + input.getApiId();
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, byOrganizationAndApi.getOrganizationId());
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApiRegistrationList.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(byOrganizationAndApi);
    }

    public List<ApiRegistration> findRegistrationsForOrganization(UUID organizationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registrations";
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApiRegistrationList.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(organizationId);
    }

    public ApiRegistrationSteps findApiRegistrationSteps(ByOrganizationAndApi byOrganizationAndApi) {
        RequestBuilder<ByOrganizationAndApi> resultsRequestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registration-step-results/" +
                    byOrganizationAndApi.getApiId().toString();
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, input.getOrganizationId());
        };

        ApiRegistrationStepResultsList stepResults =
                new RequestExecutor.Builder<>(resultsRequestBuilder, httpClient, ApiRegistrationStepResultsList.class)
                        .addResponseValidator(new ResponseNotEmptyValidator())
                        .addResponseValidator(new ExpectedHttpCodesValidator(200))
                        .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                        .build()
                        .execute(byOrganizationAndApi);

        RequestBuilder<ByOrganizationAndApi> definitionsRequestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registration-step-definitions/" +
                    byOrganizationAndApi.getApiId().toString();
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, input.getOrganizationId());
        };

        ApiRegistrationStepDefinitionList stepDefinitions =
                new RequestExecutor.Builder<>(definitionsRequestBuilder, httpClient, ApiRegistrationStepDefinitionList.class)
                        .addResponseValidator(new ResponseNotEmptyValidator())
                        .addResponseValidator(new ExpectedHttpCodesValidator(200))
                        .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                        .build()
                        .execute(byOrganizationAndApi);

        return new ApiRegistrationSteps(stepResults, stepDefinitions);
    }
}
