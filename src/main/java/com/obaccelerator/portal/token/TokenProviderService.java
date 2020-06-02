package com.obaccelerator.portal.token;

import com.obaccelerator.common.token.TokenGenerator;
import com.obaccelerator.portal.config.ObaPortalProperties;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.obaccelerator.common.ObaConstant.ROLE_PORTAL_ADMIN;
import static com.obaccelerator.common.ObaConstant.ROLE_PORTAL_ORGANIZATION;

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

    private String getOrganizationToken(UUID organizationId) {
        return organizationTokenGenerator.generateInternalToken(organizationId.toString(), ROLE_PORTAL_ORGANIZATION);
    }

    private String getObaAdminToken() {
        return obaAdminTokenGenerator.generateToken(ROLE_PORTAL_ADMIN);
    }

    public <T extends HttpRequestBase> T addOrganizationToken(T request, UUID organizationId) {
        request.addHeader("Authorization", "Bearer " + getOrganizationToken(organizationId));
        return request;
    }
}