package org.trianglex.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.trianglex.common.constant.PropertiesConstant.SPRING_MVC_XSS_FILTER_ORDERED;

public class XssRequestFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(XssRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(new XssHttpServletRequest(request), response);
    }

    @Override
    public int getOrder() {
        return SPRING_MVC_XSS_FILTER_ORDERED;
    }
}
