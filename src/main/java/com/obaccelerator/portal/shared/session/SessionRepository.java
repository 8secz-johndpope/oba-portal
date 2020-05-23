package com.obaccelerator.portal.shared.session;

import com.obaccelerator.common.date.DateUtil;
import com.obaccelerator.portal.session.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static com.obaccelerator.portal.ObaPortalApplication.SESSION_DURATION_MINUTES;

@Repository
public class SessionRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SessionRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    void createSession(UUID newId, UUID portalUserId) {
        String now = DateUtil.currentDateTimeUtcForMysql();
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.session (id, portal_user_id, last_used, created) " +
                        "VALUES (UUID_TO_BIN(:newId), UUID_TO_BIN(:portalUserId), :lastUsed, :created)",
                new MapSqlParameterSource(new HashMap<String, Object>() {
                    {
                        put("newId", newId.toString());
                        put("portalUserId", portalUserId.toString());
                        put("lastUsed", now);
                        put("created", now);

                    }
                }));
    }

    void updateSessionLastUsed(UUID sessionId) {
        String now = DateUtil.currentDateTimeUtcForMysql();
        namedParameterJdbcTemplate.update("UPDATE obaportal.session SET last_used = :now WHERE id = UUID_TO_BIN(:sessionId)",
                new MapSqlParameterSource(new HashMap<String, Object>() {
                    {
                        put("sessionId", sessionId.toString());
                        put("now", now);
                    }
                }));
    }

    Optional<Session> findActiveSession(UUID sessionId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT s.*, " +
                                "BIN_TO_UUID(s.id) as session_id, " +
                                "BIN_TO_UUID(pu.organization_id) as organization_id, " +
                                "BIN_TO_UUID(pu.id) as real_portal_user_id " +
                                "FROM session s INNER JOIN portal_user pu on s.portal_user_id = pu.id " +
                                "WHERE s.id = UUID_TO_BIN(:sessionId) " +
                                "AND s.last_used > NOW() - INTERVAL :duration MINUTE",
                        new HashMap<>() {
                            {
                                put("sessionId", sessionId.toString());
                                put("duration", SESSION_DURATION_MINUTES);
                            }
                        }, (rs, rowNum) -> new Session(
                                UUID.fromString(rs.getString("session_id")),
                                UUID.fromString(rs.getString("real_portal_user_id")),
                                UUID.fromString(rs.getString("organization_id")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("last_used")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

    void deleteSession(UUID sessionId) {
        namedParameterJdbcTemplate.update("DELETE FROM obaportal.session WHERE id = UUID_TO_BIN(:sessionId)",
                new HashMap<String, Object>() {
                    {
                        put("sessionId", sessionId.toString());
                    }
                }
        );
    }
}
