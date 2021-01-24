package org.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScans({
        @ComponentScan(basePackages = "org.example.models"),
        @ComponentScan(basePackages = "org.example.dao")
})
public class TestConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

    @Bean
    public DataSource dataSource(){
        try {
            EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
            logger.debug("DBCP-Test DataSource was created");
        return dbBuilder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .build();
        } catch (Exception e){
            logger.error("Embedded DataSource cannot be created", e);
            return null;
        }
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        logger.debug("Bean namedParameterJdbcTemplate for tests was created");
        return new NamedParameterJdbcTemplate(dataSource());
    }
}
