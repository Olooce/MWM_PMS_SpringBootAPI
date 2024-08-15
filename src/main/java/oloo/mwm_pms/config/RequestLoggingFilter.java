package oloo.mwm_pms.config;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter, RequestLoggingFilter_interface {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String origin = request.getHeader("Origin");
        String userAgent = request.getHeader("User-Agent");
        String remoteAddr = request.getRemoteAddr();

        // Log request details
        System.out.println("Request Origin: " + origin);
        System.out.println("User Agent: " + userAgent);
        System.out.println("Client IP: " + remoteAddr);

        // Continue with the next filter in the chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}

