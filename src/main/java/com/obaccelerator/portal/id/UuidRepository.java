package com.obaccelerator.portal.id;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
@Repository
public class UuidRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UuidRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public UUID newId() {
        final UUID[] uuid = {null};
        namedParameterJdbcTemplate.query("SELECT UUID()", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                uuid[0] =  UUID.fromString(resultSet.getString(1));
            }
        });

        log.info("Generated uuid {}", uuid);
        return uuid[0];
    }
}
