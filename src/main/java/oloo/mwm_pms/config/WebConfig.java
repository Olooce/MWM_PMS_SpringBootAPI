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
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .exposedHeaders("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .allowCredentials(true)
                .allowedOrigins(getAllowedOrigins().toArray(new String[0]));
    }

    private Set<String> getAllowedOrigins() {
        Set<String> allowedOrigins = new HashSet<>(List.of(
                "http://localhost:3000",
                "https://your-client-domain.com",
                "https://delicate-clearly-roughy.ngrok-free.app"
        ));
        for (String blockedOrigin : blockedOrigins) {
            allowedOrigins.remove(blockedOrigin);
        }
        return allowedOrigins;
    }
}
