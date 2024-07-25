package oloo.mwm_pms.session;

import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    // Validate the session token (cookie.getValue())
                    if (isValidSessionToken(cookie.getValue())) {
                        chain.doFilter(request, response);
                        return;
                    }
                }
            }
        }

        // If no valid session token found, respond with 401 Unauthorized
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean isValidSessionToken(String token) {
        // Implement your session token validation logic here
        return "valid_session_token".equals(token);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
