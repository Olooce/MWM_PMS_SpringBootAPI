package oloo.mwm_pms.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    private final Set<String> blockedOrigins = new HashSet<>();
//
//    public WebConfig() {
//        blockedOrigins.add("http://example.com");
//        blockedOrigins.add("http://malicious.com");
//    }
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .exposedHeaders("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
//                .allowCredentials(false)
//                .allowedOrigins("*");
////                .allowedOrigins(getAllowedOrigins().toArray(new String[0]));
//    }
//
//    private Set<String> getAllowedOrigins() {
//        Set<String> allowedOrigins = new HashSet<>(List.of(
//                "https://delicate-clearly-roughy.ngrok-free.app",
//                "http://localhost:3000",
//                "https://your-client-domain.com"
//
//        ));
//        for (String blockedOrigin : blockedOrigins) {
//            allowedOrigins.remove(blockedOrigin);
//        }
//        return allowedOrigins;
//    }
//
//    @Bean
//    public OncePerRequestFilter requestLoggingFilter() {
//        return new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain filterChain)
//                    throws ServletException, IOException {
//
//                String origin = request.getHeader("Origin");
//                String userAgent = request.getHeader("User-Agent");
//                String remoteAddr = request.getRemoteAddr();
//
//                System.out.println("Request Origin: " + origin);
//                System.out.println("User Agent: " + userAgent);
//                System.out.println("Client IP: " + remoteAddr);
//
//                filterChain.doFilter(request, response);
//            }
//        };
//    }
//}



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://delicate-clearly-roughy.ngrok-free.app")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}

