package com.umut.project.database.mywebservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        Path profileDir = Paths.get(uploadDir, "profile").toAbsolutePath().normalize();
        String location = profileDir.toUri().toString();
        if(!location.endsWith("/"))
        {
            location += "/";
        }
        registry.addResourceHandler("/api/v1/assets/profile/**")
                .addResourceLocations(location);
    }
}
