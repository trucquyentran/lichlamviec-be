package com.example.qllichlamviec.config;

import org.springframework.beans.factory.annotation.Value;

public class CustomPropertiesConfig {

    @Value("${environments.url.upload}")
    private String forderPath;

    public String getForderPath() {
        return forderPath;
    }
}
