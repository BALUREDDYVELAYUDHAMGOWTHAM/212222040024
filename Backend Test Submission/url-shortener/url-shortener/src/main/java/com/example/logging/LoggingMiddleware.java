package com.example.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class LoggingMiddleware implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        System.out.println("Request: " + http.getMethod() + " " + http.getRequestURI());
        chain.doFilter(request, response);
    }
}