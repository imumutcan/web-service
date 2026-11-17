package com.umut.project.database.mywebservice.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String host;
    private String username;
    private String password;
    private int port;
}
