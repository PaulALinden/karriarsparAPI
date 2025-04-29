package se.karriarspar.karriarsparAPI.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tillåt alla endpoints
                .allowedOrigins("*") // Tillåt alla origins
                .allowedMethods("GET", "POST") // Tillåtade metoder
                .allowedHeaders("*") // Tillåt alla headers
                .allowCredentials(false); // Inaktivera credentials för säkerhet med "*"
    }
}
