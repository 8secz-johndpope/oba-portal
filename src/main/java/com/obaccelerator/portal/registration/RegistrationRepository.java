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

    public UUID createRegistration(String firstName, String lastName, String email, String companyName) {
        UUID uuid = UUID.randomUUID();
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.customer_registration (id, first_name, last_name, " +
                "email, company_name, created) " +
                "VALUES (:id, :firstName, :lastName, :email, :companyName, :created)", new HashMap<String, Object>() {
            {
                put("id", uuid.toString());
                put("firstName", firstName);
                put("lastName", lastName);
                put("email", email);
                put("companyName", companyName);
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });
        return uuid;
    }

    public UUID createRegistration(String firstName, String lastName, String email, String companyName, BotEvaluationResult botEvaluationResult) {
        UUID uuid = UUID.randomUUID();
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.customer_registration (id, first_name, last_name, " +
                "email, company_name, likely_a_bot, full_request, created) " +
                "VALUES (:id, :firstName, :lastName, :email, :companyName, :likelyABot, :fullRequest, :created)", new HashMap<String, Object>() {
            {
                put("id", uuid.toString());
                put("firstName", firstName);
                put("lastName", lastName);
                put("email", email);
                put("companyName", companyName);
                put("likelyABot", botEvaluationResult.isLikelyABot());
                put("fullRequest", botEvaluationResult.getFullRequest());
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        });
        return uuid;
    }

    public Optional<Registration> findRegistration(UUID registrationId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT * FROM obaportal.customer_registration WHERE id = :registrationId",
                        new HashMap<String, Object>() {
                            {
                                put("registrationId", registrationId.toString());
                            }
                        }, (rs, rowNum) -> new Registration(UUID.fromString(rs.getString("id")), rs.getString("first_name"),
                                rs.getString("last_name"), rs.getString("company_name"), rs.getString("email"),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

}
