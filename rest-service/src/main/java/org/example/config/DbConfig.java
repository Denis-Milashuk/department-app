package org.example.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


@Configuration
/*@PropertySources({
        @PropertySource("classpath:sqlQuery.properties"),
        @PropertySource("classpath:jdbc.properties"),
})*/
@PropertySource("classpath:jdbc.properties")
@ComponentScan(basePackages = "org.example")
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

/*    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }*/

    @Lazy
    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource(){
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            return dataSource;
        }catch (Exception e){
            logger.error("DBCP DataSource bean cannot be created",e);
            return null;
        }
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource());
    }

}
