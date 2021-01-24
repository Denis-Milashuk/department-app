package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.client.RestTemplate;


@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("org.example.config")
public class ClientRestConfig {

    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(){
        var httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        var httpClient = HttpClientBuilder.create().build();
        httpRequestFactory.setHttpClient(httpClient);
        return httpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(httpRequestFactory());
    }
}
