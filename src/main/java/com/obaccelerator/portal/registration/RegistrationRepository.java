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

    void createRegistration(UUID newId, String cognitoId, String organizationName) {
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.registration (id, cognito_user_id, organization_name, created) " +
                "VALUES (UUID_TO_BIN(:newId), :cognitoId, :organizationName, :created)", new HashMap<String, Object>() {
            {
                put("newId", newId.toString());
                put("cognitoId", cognitoId);
                put("organizationName", organizationName);
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });
    }

    void createRegistration(UUID newId, String cognitoId, String organizationName, BotEvaluationResult botEvaluationResult) {
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.registration (id, cognito_user_id, organization_name, " +
                "likely_a_bot, full_request, created) " +
                "VALUES (UUID_TO_BIN(:newId), :cognitoId, :organizationName, :likelyABot, :fullRequest, :created)", new HashMap<String, Object>() {
            {
                put("newId", newId.toString());
                put("cognitoId", cognitoId);
                put("organizationName", organizationName);
                put("likelyABot", botEvaluationResult.isLikelyABot());
                put("fullRequest", botEvaluationResult.getFullRequest());
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });
    }

    Optional<Registration> findRegistration(UUID registrationId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT  registration.*, BIN_TO_UUID(id) as realId FROM obaportal.registration WHERE id = UUID_TO_BIN(:id)",
                        new HashMap<String, Object>() {
                            {
                                put("id", registrationId.toString());
                            }
                        }, (rs, rowNum) -> new Registration(
                                UUID.fromString(rs.getString("realId")),
                                UUID.fromString(rs.getString("cognito_user_id")),
                                rs.getString("organization_name"),
                                rs.getString("promoted_to_organization") == null ? null :
                                        UUID.fromString(rs.getString("promoted_to_organization")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

    Optional<Registration> findRegistrationByCognitoId(String cognitoId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT registration.*, BIN_TO_UUID(id) as realId FROM registration WHERE cognito_user_id = :cognitoId",
                        new HashMap<String, Object>() {
                            {
                                put("cognitoId", cognitoId);
                            }
                        }, (rs, rowNum) -> new Registration(
                                UUID.fromString(rs.getString("realId")),
                                UUID.fromString(rs.getString("cognito_user_id")),
                                rs.getString("organization_name"),
                                rs.getString("promoted_to_organization") == null ? null :
                                        UUID.fromString(rs.getString("promoted_to_organization")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

    void setOrganizationId(UUID registrationId, UUID organizationId) {
        namedParameterJdbcTemplate.update("UPDATE registration SET registration.promoted_to_organization = :organizationId " +
                "WHERE registration.id = :registrationId", new HashMap<String, Object>() {{
                    put("organizationId", organizationId);
                    put("registrationId", registrationId);
        }});
    }

}
