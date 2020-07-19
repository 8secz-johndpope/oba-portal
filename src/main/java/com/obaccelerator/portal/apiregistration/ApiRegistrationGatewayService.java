package com.obaccelerator.portal.apiregistration;

import com.obaccelerator.common.error.EntityNotFoundException;
import com.obaccelerator.common.form.SubmittedForm;
import com.obaccelerator.common.http.*;
import com.obaccelerator.common.model.organization.OrganizationId;
import com.obaccelerator.portal.config.ObaPortalProperties;
import com.obaccelerator.portal.token.TokenProviderService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registrations?apiId=" + input.getApiId();
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

    public ApiRegistration findRegistrationForOrganization(UUID organizationId, UUID registrationId) {
        List<ApiRegistration> registrationsForOrganization = findRegistrationsForOrganization(organizationId);
        return registrationsForOrganization.stream().filter(r -> r.getId().equals(registrationId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(ApiRegistration.class, registrationId));
    }

    public ApiRegistrationSteps findApiRegistrationSteps(ByOrganizationAndApi byOrganizationAndApi) {
        RequestBuilder<ByOrganizationAndApi> resultsRequestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registration-steps/" +
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

        Optional<ApiRegistrationStepDefinition> nextStepOptional = stepDefinitions.stream()
                .filter(def -> stepResults.stream()
                        .noneMatch(res -> res.getStepNr() == def.getStepNr()))
                .min(Comparator.comparingInt(ApiRegistrationStepDefinition::getStepNr));


        return new ApiRegistrationSteps(stepResults, stepDefinitions,
                nextStepOptional.orElse(null));
    }

    public ApiRegistrationStep submitRegistrationStep(UUID organizationId, UUID apiId, SubmittedForm submittedForm) {
        RequestBuilder<SubmittedForm> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registration-steps/" + apiId.toString();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new JsonHttpEntity<>(submittedForm));
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApiRegistrationStep.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(submittedForm);
    }

    public ApiRegistration submitUpdateRegistrationStep(UUID organizationId, UUID apiRegistrationId, SubmittedForm submittedForm) {
        RequestBuilder<SubmittedForm> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registration-update-step-definition/" + apiRegistrationId.toString();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new JsonHttpEntity<>(submittedForm));
            return tokenProviderService.addOrganizationToken(httpPost, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApiRegistration.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(submittedForm);
    }

    public ApiRegistrationStepDefinition getUpdateRegistrationStepDefinition(UUID organizationId, UUID apiIRegistrationId) {
        RequestBuilder<UUID> requestBuilder = (input) -> {
            String url = obaPortalProperties.getObaBaseUrl() + "/api-registration-update-step-definition/" + apiIRegistrationId.toString();
            HttpGet httpGet = new HttpGet(url);
            return tokenProviderService.addOrganizationToken(httpGet, organizationId);
        };

        return new RequestExecutor.Builder<>(requestBuilder, httpClient, ApiRegistrationStepDefinition.class)
                .addResponseValidator(new ResponseNotEmptyValidator())
                .addResponseValidator(new ExpectedHttpCodesValidator(200))
                .logRequestResponsesOnError(obaPortalProperties.isLogRequestsAndResponsesOnError())
                .build()
                .execute(organizationId);
    }
}
