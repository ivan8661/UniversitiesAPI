package com.scheduleapigateway.apigateway.Controllers;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String xRequestId = ((HttpServletRequest) servletRequest).getHeader("X-Request-Id");
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(xRequestId == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
                filterChain.doFilter(servletRequest, response);
        }
    }
    @Override
    public void destroy() {

    }
}
