package com.langer.server.config.filters;

import com.langer.server.api.LangerApiGlobal;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
class SecurityFilter implements Filter
{
  private final String secret;

  public SecurityFilter(String secret)
  {
    this.secret = secret;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException
  {
  }

  @Override
  public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
  {
    log.info("##### SEC FILTER CALLED");

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    String reqSecret = request.getHeader(LangerApiGlobal.HEADER_AUTH);
    if (reqSecret == null || !reqSecret.equals(secret))
    {
      log.warn("got unauthorized request from {} to {}", servletRequest.getRemoteHost(), ((HttpServletRequest) servletRequest).getRequestURI());
      if (reqSecret != null)
      {
        log.warn("secret: {}, header: {}",secret, reqSecret);
      }
      throw new ApplicationSecurityException("unauthorized");
    }

    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy(){}
}