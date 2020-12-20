package com.langer.server.config.filters;

import com.langer.server.LangerRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class LangerContextFilter implements Filter
  {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
      HttpServletRequest request = (HttpServletRequest) servletRequest;

      Map headers = Collections.list((Enumeration<String>)request.getHeaderNames())
              .stream()
              .collect(Collectors.toMap(
                      k -> k,
                      v -> Collections.list(request.getHeaders(v)))
              );

      RequestContextHolder.getRequestAttributes().setAttribute(LangerRequestContext.LANGER_REQUEST_HEADERS_ATTR_NAME, headers, RequestAttributes.SCOPE_REQUEST);

      filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy(){}
  }