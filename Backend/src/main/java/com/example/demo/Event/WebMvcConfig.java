package com.example.demo.Event;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/images/event/**")
            .addResourceLocations(
                "file:/C:/spring_uploads/images/event/",
                "file:/C:/uploads/event/"
            );
}
}

