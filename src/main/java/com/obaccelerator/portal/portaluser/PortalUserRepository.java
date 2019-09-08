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
                namedParameterJdbcTemplate.query("SELECT * FROM obaportal.portal_user WHERE cognito_user_id = :cognitoId",
                        new HashMap<String, Object>() {
                            {
                                put("cognitoId", cognitoId);
                            }
                        }, (rs, rowNum) -> new PortalUser(rs.getInt("id"), rs.getString("cognito_user_id"),
                                UUID.fromString(rs.getString("organization_id")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("first_login")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

    public int createPortalUser(String cognitoUserId, UUID organizationId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(new HashMap<String, Object>() {
            {
                put("cognitoUserId", cognitoUserId);
                put("organizationId", organizationId.toString());
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });

        namedParameterJdbcTemplate.update("INSERT INTO obaportal.portal_user (cognito_user_id, organization_id, created) " +
                "VALUES (:cognitoUserId,:organizationId, :created)", parameterSource, keyHolder);


        return keyHolder.getKey().intValue();
    }
}
