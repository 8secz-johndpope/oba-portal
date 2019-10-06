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

    public void createOrganization(UUID newId, String name) {
        namedParameterJdbcTemplate.update("INSERT INTO obaportal.organization (id, name, created) " +
                "VALUES (UUID_TO_BIN(:id), :name, :created)", new MapSqlParameterSource(new HashMap<String, Object>() {
            {
                put("id", newId.toString());
                put("name", name);
                put("created", DateUtil.currentDateTimeUtcForMysql());
            }
        }));
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

    public Organization findOrganization(UUID id) {
        return DataAccessUtils.singleResult(
                namedParameterJdbcTemplate.query("SELECT BIN_TO_UUID(organization.id) as realId, organization.* FROM " +
                                "obaportal.organization WHERE id = UUID_TO_BIN(:id)",
                        new HashMap<String, Object>() {
                            {
                                put("id", id.toString());
                            }
                        }, (rs, rowNum) -> new Organization(
                                UUID.fromString(rs.getString("realId")),
                                rs.getString("name"),
                                rs.getString("vat_number"),
                                rs.getString("street"),
                                rs.getString("street_number"),
                                DateUtil.mysqlUtcDateTimeToOffsetDateTime(rs.getString("created")))));
    }
}


