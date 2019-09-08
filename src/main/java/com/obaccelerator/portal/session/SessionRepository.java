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

    public UUID createSession(int portalUserId) {
        UUID uuid = UUID.randomUUID();
        String now = DateUtil.currentDateTimeUtcForMysql();
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.session (id, portal_user_id, last_used, created) " +
                "VALUES (:id, :portalUserId, :lastUsed, :created)", new MapSqlParameterSource(new HashMap<String, Object>() {
            {
                put("id", uuid.toString());
                put("portalUserId", portalUserId);
                put("lastUsed", now);
                put("created", now);
            }
        }));
        return uuid;
    }

    public void updateSessionLastUsed(UUID sessionId) {
        String now = DateUtil.currentDateTimeUtcForMysql();
        namedParameterJdbcTemplate.update("UPDATE obaportal.session SET last_used = :now WHERE id = :sessionId",
                new MapSqlParameterSource(new HashMap<String, Object>() {
                    {
                        put("sessionId", sessionId.toString());
                        put("now", now);
                    }
                }));
    }

    public Optional<Session> findValidSession(UUID sessionId) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT * FROM obaportal.session WHERE id = :sessionId",
                        new HashMap<String, Object>() {
                            {
                                put("id", sessionId.toString());
                            }
                        }, (rs, rowNum) -> new Session(
                                UUID.fromString(rs.getString("id")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("last_used")),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }

    public void deleteSession(UUID sessionId) {
        namedParameterJdbcTemplate.update("DELETE FROM obaportal.session WHERE id = :sessionId", new HashMap<String, Object>() {
                    {
                        put("sessionId", sessionId.toString());
                    }
                }
        );
    }
}
