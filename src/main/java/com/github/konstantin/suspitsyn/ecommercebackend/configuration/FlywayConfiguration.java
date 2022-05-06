package com.github.konstantin.suspitsyn.ecommercebackend.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {
    @Autowired
    public FlywayConfiguration(DataSource dataSource) {
        Flyway.configure().dataSource(dataSource)
                .baselineOnMigrate(true)
                .schemas("public")
                .load().migrate();
    }
}
