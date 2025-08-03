package com.metanet.study.global.filter;

import java.io.IOException;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HeaderLoggingFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(HeaderLoggingFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    Enumeration<String> headerNames = request.getHeaderNames();
    if (headerNames != null) {
      logger.info("HTTP Request Headers:");
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        logger.info("{}: {}", headerName, headerValue);
      }
    }
    filterChain.doFilter(request, response);
  }
}
