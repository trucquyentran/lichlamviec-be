package com.example.qllichlamviec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class CustomPropertiesConfig {

    @Value("${environments.url.upload}")
    private String forderPath;

    public String getForderPath() {
        return forderPath;
    }
}
