package com.obaccelerator.portal.organization;

import com.obaccelerator.common.date.DateUtil;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrganizationRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrganizationRepository(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public UUID createOrganization(String name) {
        UUID id = UUID.randomUUID();
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.organization (id, name, created) " +
                "VALUES (:id, :name, :created)", new MapSqlParameterSource(new HashMap<String, Object>() {
            {
                put("id", id.toString());
                put("name", name);
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        }));
        return id;
    }

    public void deleteOrganization(UUID id) {
        namedParameterJdbcTemplate.update("DELETE FROM obaportal.organization WHERE id = :id", new HashMap<String, Object>() {
                    {
                        put("id", id.toString());
                    }
                }
        );
    }

    public UUID updateOrganization(String name, String vatNumber, String street, String streetNumber) {
        UUID id = UUID.randomUUID();
        namedParameterJdbcTemplate.update("UPDATE obaportal.organization SET name = :name, vat_number = :vatNumber, " +
                "street = :street, street_number = :streetNumber", new MapSqlParameterSource(new HashMap<String, Object>() {
            {
                put("name", name);
                put("vatNumber", vatNumber);
                put("street", street);
                put("streetNumber", streetNumber);
            }
        }));
        return id;
    }

    public Optional<Organization> findOrganization(UUID id) {
        return Optional.ofNullable(DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT * FROM obaportal.organization WHERE id = :id",
                        new HashMap<String, Object>() {
                            {
                                put("id", id.toString());
                            }
                        }, (rs, rowNum) -> new Organization(UUID.fromString(rs.getString("id")),
                                rs.getString("name"), rs.getString("vat_number"), rs.getString("street"),
                                rs.getString("street_number"),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created"))))));
    }
}


