package com.obaccelerator.portal.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Getter
@ConfigurationProperties("obaportal")
public class ObaPortalProperties {

    @Value("${ext_obaportal.db.user}")
    private String mysqlUser;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${ext_obaportal.db.password}")
    private String mysqlPassword;

    @Value("${ext_oba-portal-server-cert.keystore.password}")
    private String serverCertKeystorePassword;

    @Value("${tls.server.keystore.path}")
    public String serverCertKeystorePath;

    @Value("${obaportal.db.name}")
    private String dbName;

    @Value("${cognito.user.pool.id}")
    private String cognitoUserPoolId;

    @Value("${cognito.aws.region}")
    private String cognitoAwsRegion;

    @Value("${oba.base.url}")
    private String obaBaseUrl;

    @Value("${log.requests.responses.on.error}")
    private boolean logRequestsAndResponsesOnError;

    @Value("${oba.admin.token.signing.keystore.path}")
    private String adminTokenKeyStorePath;

    @Value("${organization.token.signing.keystore.path}")
    private String organizationTokenKeyStorePath;

    @Value("${ext_organization-admin-token-signing.keystore.password}")
    private String organizationTokenKeyStorePassword;

    @Value("${ext_oba-admin-token-signing.keystore.password}")
    private String adminTokenKeyStorePassword;

    @Value("${application.domain}")
    private String applicationDomain;

    @Value("${server.servlet.context-path}")
    private String applicationContextPath;

    public String cognitoPublicKeysUrl() {
        return String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", cognitoAwsRegion, cognitoUserPoolId);
    }
}
