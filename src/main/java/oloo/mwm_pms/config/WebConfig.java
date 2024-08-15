package oloo.mwm_pms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                .allowedOrigins(getAllowedOrigins().toArray(new String[0])); // Apply allowed origins
    }

    private Set<String> getAllowedOrigins() {
        Set<String> allowedOrigins = new HashSet<>(List.of(
                "http://localhost:3000", // Replace with your client URL
                "https://your-client-domain.com", // Add other allowed origins here
                "https://delicate-clearly-roughy.ngrok-free.app" // Add your ngrok URL here
        ));
        for (String blockedOrigin : blockedOrigins) {
            allowedOrigins.remove(blockedOrigin);
        }
        return allowedOrigins;
    }
}
