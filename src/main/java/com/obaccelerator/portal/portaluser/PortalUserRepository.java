package com.obaccelerator.portal.portaluser;

import com.obaccelerator.common.date.DateUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;

@Repository
public class PortalUserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PortalUserRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<PortalUser> findPortalUserByCognitoId(String cognitoId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT BIN_TO_UUID(u.id) as realId,  " +
                                "BIN_TO_UUID(u.organization_id) as realOrganizationId, u.*, r.role " +
                                "FROM obaportal.portal_user u INNER JOIN obaportal.portal_user_2_role r " +
                                "ON r.portal_user_id = u.id " +
                                "WHERE cognito_user_id = :cognitoId",
                        new HashMap<>() {
                            {
                                put("cognitoId", cognitoId);
                            }
                        }, new PortalUserWithRolesExtractor())));
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

    public void createRoleLink(UUID portalUserId, String role) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(new HashMap<>() {
            {
                put("portalUserId", portalUserId.toString());
                put("role", role);
            }
        });
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.portal_user_2_role(portal_user_id, role) " +
                "VALUES (UUID_TO_BIN(:portalUserId), :role)", parameterSource);
    }

    public Optional<PortalUser> findById(UUID id, UUID organizationId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT BIN_TO_UUID(u.id) as realId,  " +
                                "BIN_TO_UUID(u.organization_id) as realOrganizationId, u.*, r.role " +
                                "FROM obaportal.portal_user u INNER JOIN obaportal.portal_user_2_role r " +
                                "ON r.portal_user_id = u.id " +
                                "WHERE u.id = UUID_TO_BIN(:id) and u.organization_id = UUID_TO_BIN(:orgId)",
                        new HashMap<>() {
                            {
                                put("id", id.toString());
                                put("orgId", organizationId.toString());
                            }
                        }, new PortalUserWithRolesExtractor())));
    }

    private static class PortalUserWithRolesExtractor implements ResultSetExtractor<List<PortalUser>> {

        @Override
        public List<PortalUser> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<String> roles = new ArrayList<>();
            UUID id = null;
            String cognitoUserId = null;
            UUID organizationId = null;
            OffsetDateTime firstLogin = null;
            OffsetDateTime created = null;
            boolean found = false;

            while (rs.next()) {
                found = true;
                id = UUID.fromString(rs.getString("realId"));
                cognitoUserId = rs.getString("cognito_user_id");
                organizationId = UUID.fromString(rs.getString("realOrganizationId"));
                firstLogin = DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("first_login"));
                created = DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"));
                roles.add(rs.getString("role"));
            }

            return found ? Collections.singletonList(new PortalUser(id, cognitoUserId, organizationId, firstLogin, created, roles)) : null;
        }
    }
}
