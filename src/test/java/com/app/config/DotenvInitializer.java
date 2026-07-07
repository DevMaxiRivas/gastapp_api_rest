package com.app.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        Map<String, Object> envVars = new HashMap<>();
        dotenv.entries().forEach(entry -> envVars.put(entry.getKey(), entry.getValue()));

        // Insert all environment variables to spring
        applicationContext.getEnvironment().getPropertySources().addFirst(
                new MapPropertySource("dotenvProperties", envVars)
        );
    }
}