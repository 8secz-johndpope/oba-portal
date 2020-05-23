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
    public String tlsServerKeystorePath;

    @Value("${obaportal.db.name}")
    private String dbName;

    @Value("${cognito.user.pool.id}")
    private String cognitoUserPoolId;

    @Value("${cognito.aws.region}")
    private String cognitoAwsRegion;

    @Value("${oba.base.url}")
    private String obaBaseUrl;

    @Value("${log.requests.responses.on.error.for.organizations}")
    private boolean logRequestsResponsesOnErrorForOrganizations;

    public String cognitoPublicKeysUrl() {
        return String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", cognitoAwsRegion, cognitoUserPoolId);
    }

}
