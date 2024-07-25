package oloo.mwm_pms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
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
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers");
        
        registry.addMapping("/**").checkOrigin(origin -> {
            if (blockedOrigins.contains(origin)) {
                return false;
            }
            return true;
        });
    }
}

