package com.obaccelerator.portal.session;

import com.obaccelerator.common.date.DateUtil;
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
                namedParameterJdbcTemplate.query("SELECT BIN_TO_UUID(id) realId, session.* " +
                                "FROM obaportal.session " +
                                "WHERE id = UUID_TO_BIN(:sessionId) " +
                                "AND last_used > NOW() - INTERVAL 30 MINUTE",
                        new HashMap<String, Object>() {
                            {
                                put("sessionId", sessionId.toString());
                            }
                        }, (rs, rowNum) -> new Session(
                                UUID.fromString(rs.getString("realId")),
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
