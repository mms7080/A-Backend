package com.example.demo.Event;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://localhost:9999/images/event/** → C:/uploads/event/**
        registry.addResourceHandler("/images/event/**")
                .addResourceLocations("file:uploads/event/");
    }
}

