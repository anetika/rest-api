package com.epam.esm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


/**
 * The class that configures test component of core.
 */
@Configuration
@Profile("dev")
@ComponentScan("com.epam.esm")
public class TestCoreConfiguration {
    private static final String ENCODING = "UTF-8";

    @Value("classpath:scripts/create_schema.sql")
    private String createSchemaScriptPath;
    @Value("classpath:scripts/data.sql")
    private String dataScriptPath;

    @Bean
    public EmbeddedDatabase embeddedDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding(ENCODING)
                .addScript(createSchemaScriptPath)
                .addScript(dataScriptPath)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(EmbeddedDatabase embeddedDatabase) {
        return new JdbcTemplate(embeddedDatabase);
    }
}
