package com.obaccelerator.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


/**
 * This JDBC config is almost empty, because a PlatformTransactionManager and a NamedParameterJdbcTemplate
 * are already auto-configured
 * <p>
 * See JdbcTemplateAutoConfiguration for JDBC template
 * See DataSourceTransactionManagerAutoConfiguration for the transaction manager
 */
@Configuration
public class MysqlConfig {

    private ObaPortalProperties obaPortalProperties;

    public MysqlConfig(ObaPortalProperties obaPortalProperties) {
        this.obaPortalProperties = obaPortalProperties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(obaPortalProperties.getDbUrl()+ "/" + obaPortalProperties.getDbName());
        dataSource.setUsername(obaPortalProperties.getMysqlUser());
        dataSource.setPassword(obaPortalProperties.getMysqlPassword());
        return dataSource;
    }
}
