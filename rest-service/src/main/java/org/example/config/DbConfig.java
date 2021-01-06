package org.example.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;



@Configuration
@PropertySource("classpath:jdbc.properties")
@ComponentScans({
        @ComponentScan(basePackages = "org.example.models"),
        @ComponentScan(basePackages = "org.example.dao")
})
public class DbConfig {

    private static final Logger logger = LoggerFactory.getLogger(DbConfig.class.getName());

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.userName}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource(){
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            logger.debug("DBCP DataSource was created");
            return dataSource;
        }catch (Exception e){
            logger.error("DBCP DataSource bean cannot be created",e);
            return null;
        }
    }


    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        logger.debug("Bean namedParameterJdbcTemplate was created");
        return new NamedParameterJdbcTemplate(dataSource());
    }

}
