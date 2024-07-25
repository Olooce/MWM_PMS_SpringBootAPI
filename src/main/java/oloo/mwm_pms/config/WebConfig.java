package oloo.mwm_pms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Set<String> blockedOrigins = new HashSet<>();

    public WebConfig() {
        blockedOrigins.add("http://example.com");
        blockedOrigins.add("http://malicious.com");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .allowCredentials(true)
                .allowedOriginPatterns("*") // Allow all origins by default
                .allowedOriginPatterns(getAllowedOrigins().toArray(new String[0])); // Apply allowed origins pattern

    }

    private Set<String> getAllowedOrigins() {
        Set<String> allowedOrigins = new HashSet<>(List.of("*")); // Start with all origins allowed
        for (String blockedOrigin : blockedOrigins) {
            allowedOrigins.remove(blockedOrigin);
        }
        return allowedOrigins;
    }
}
