package com.obaccelerator.portal.token;

import com.obaccelerator.common.token.ApiToken;
import com.obaccelerator.common.token.TokenGenerator;
import com.obaccelerator.portal.config.ObaPortalProperties;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.obaccelerator.common.ObaConstant.ADMIN;

;

@Service
public class TokenProviderService {

    private final TokenGenerator organizationTokenGenerator;
    private final TokenGenerator obaAdminTokenGenerator;

    public TokenProviderService(ObaPortalProperties obaPortalProperties) {
        String organizationTokenKeyStorePath = obaPortalProperties.getOrganizationTokenKeyStorePath();
        String organizationTokenKeyStorePassword = obaPortalProperties.getOrganizationTokenKeyStorePassword();
        String adminTokenKeyStorePath = obaPortalProperties.getAdminTokenKeyStorePath();
        String adminTokenKeyStorePassword = obaPortalProperties.getAdminTokenKeyStorePassword();

        organizationTokenGenerator = new TokenGenerator(organizationTokenKeyStorePath, organizationTokenKeyStorePassword,
                "organization-admin-token-signing");

        obaAdminTokenGenerator = new TokenGenerator(adminTokenKeyStorePath, adminTokenKeyStorePassword,
                "oba-admin-token-signing");
    }

    private ApiToken getOrganizationToken(UUID organizationId) {
        return organizationTokenGenerator.generateOrganizationToken(organizationId.toString());
    }

    private ApiToken getObaAdminToken() {
        return obaAdminTokenGenerator.generateToken(ADMIN);
    }

    public <T extends HttpRequestBase> T addOrganizationToken(T request, UUID organizationId) {
        request.addHeader("Authorization", "Bearer " + getOrganizationToken(organizationId).getApiToken());
        return request;
    }
}
