package com.obaccelerator.portal.portaluser;

import com.obaccelerator.common.date.DateUtil;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PortalUserRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PortalUserRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<PortalUser> findPortalUserByCognitoId(String cognitoId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT BIN_TO_UUID(portal_user.id) as realId,  " +
                                "BIN_TO_UUID(portal_user.organization_id) as realOrganizationId, portal_user.* " +
                                "FROM obaportal.portal_user " +
                                "WHERE cognito_user_id = :cognitoId",
                        new HashMap<String, Object>() {
                            {
                                put("cognitoId", cognitoId);
                            }
                        }, (rs, rowNum) -> new PortalUser(
                                UUID.fromString(rs.getString("realId")),
                                rs.getString("cognito_user_id"),
                                UUID.fromString(rs.getString("realOrganizationId")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("first_login")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

    public void createPortalUser(UUID newId, String cognitoUserId, UUID organizationId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(new HashMap<String, Object>() {
            {
                put("portalUserId", newId.toString());
                put("cognitoUserId", cognitoUserId);
                put("organizationId", organizationId.toString());
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.portal_user (id, cognito_user_id, organization_id, created) " +
                "VALUES (UUID_TO_BIN(:portalUserId), :cognitoUserId, UUID_TO_BIN(:organizationId), :created)", parameterSource);
    }

    public Optional<PortalUser> findById(UUID id, UUID organizationId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT BIN_TO_UUID(portal_user.id) as realId,  " +
                                "BIN_TO_UUID(portal_user.organization_id) as realOrganizationId, portal_user.* " +
                                "FROM obaportal.portal_user " +
                                "WHERE id = UUID_TO_BIN(:id) AND organization_id = UUID_TO_BIN(:orgId)",
                        new HashMap<String, Object>() {
                            {
                                put("id", id.toString());
                                put("orgId", organizationId.toString());
                            }
                        }, (rs, rowNum) -> new PortalUser(
                                UUID.fromString(rs.getString("realId")),
                                rs.getString("cognito_user_id"),
                                UUID.fromString(rs.getString("realOrganizationId")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("first_login")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }
}
