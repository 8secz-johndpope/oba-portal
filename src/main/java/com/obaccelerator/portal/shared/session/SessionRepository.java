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
                namedParameterJdbcTemplate.query("SELECT session.*, BIN_TO_UUID(session.id) as session_id, " +
                                "BIN_TO_UUID(portal_user.organization_id) as organization_id " +
                                "FROM session INNER JOIN portal_user on session.portal_user_id = portal_user.id " +
                                "WHERE session.id = UUID_TO_BIN(:sessionId) " +
                                "AND last_used > NOW() - INTERVAL 30 MINUTE",
                        new HashMap<String, Object>() {
                            {
                                put("sessionId", sessionId.toString());
                            }
                        }, (rs, rowNum) -> new Session(
                                UUID.fromString(rs.getString("session_id")),
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
