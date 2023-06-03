package com.divineaura.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("#{'${allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;
    @Value("#{'${allowed-methods}'.split(',')}")
    private List<String> allowedMethods;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/api/**");
        allowedOrigins.forEach(origin -> corsRegistration.allowedOrigins(origin));
        allowedMethods.forEach(method -> corsRegistration.allowedMethods(method));

    }
}
