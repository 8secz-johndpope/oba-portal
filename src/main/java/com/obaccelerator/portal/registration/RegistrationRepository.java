package com.obaccelerator.portal.registration;

import com.obaccelerator.common.date.DateUtil;
import com.obaccelerator.portal.BotEvaluationResult;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RegistrationRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RegistrationRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public UUID createRegistration(String cognitoId, String organizationName) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.registration (id, cognito_user_id, organization_name, created) " +
                "VALUES (:id, :cognitoId, :organizationName, :created)", new HashMap<String, Object>() {
            {
                put("id", id);
                put("cognitoId", cognitoId);
                put("organizationName", organizationName);
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });
        return uuid;
    }

    public UUID createRegistration(String cognitoId, String organizationName, BotEvaluationResult botEvaluationResult) {
        UUID uuid = UUID.randomUUID();
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.registration (id, cognito_user_id, organization_name, " +
                "likely_a_bot, full_request, created) " +
                "VALUES (:id, :cognitoId, :organizationName, :likelyABot, :fullRequest, :created)", new HashMap<String, Object>() {
            {
                put("id", uuid.toString());
                put("cognitoId", cognitoId);
                put("organizationName", organizationName);
                put("likelyABot", botEvaluationResult.isLikelyABot());
                put("fullRequest", botEvaluationResult.getFullRequest());
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });
        return uuid;
    }

    public Optional<Registration> findRegistration(UUID registrationId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT * FROM obaportal.registration WHERE id = :id",
                        new HashMap<String, Object>() {
                            {
                                put("id", registrationId.toString());
                            }
                        }, (rs, rowNum) -> new Registration(
                                UUID.fromString(rs.getString("id")),
                                UUID.fromString(rs.getString("cognito_user_id")),
                                rs.getString("organization_name"),
                                rs.getString("promoted_to_organization") == null ? null :
                                        UUID.fromString(rs.getString("promoted_to_organization")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

    public Optional<Registration> findRegistrationByCognitoId(String cognitoId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT * FROM obaportal.registration WHERE cognito_user_id = :cognitoId",
                        new HashMap<String, Object>() {
                            {
                                put("cognitoId", cognitoId);
                            }
                        }, (rs, rowNum) -> new Registration(
                                UUID.fromString(rs.getString("id")),
                                UUID.fromString(rs.getString("cognito_user_id")),
                                rs.getString("organization_name"),
                                rs.getString("promoted_to_organization") == null ? null :
                                        UUID.fromString(rs.getString("promoted_to_organization")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

}
