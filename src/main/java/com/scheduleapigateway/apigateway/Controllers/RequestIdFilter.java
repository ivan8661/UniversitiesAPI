package com.scheduleapigateway.apigateway.Controllers;


import com.scheduleapigateway.apigateway.ApigatewayApplication;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class RequestIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String xSessionId = ((HttpServletRequest) servletRequest).getHeader("X-Session-Id");
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(xSessionId == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    @Override
    public void destroy() {

    }
}
