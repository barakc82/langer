package com.langer.server.config.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FilterConfigurator
{
  @Value("${langer.security.crm-client.secret}")
  private String secret;

  @Bean
  public FilterRegistrationBean contextFilter()
  {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    LangerContextFilter secFilter = new LangerContextFilter();

    registrationBean.setFilter(secFilter);
    registrationBean.addUrlPatterns("/api2/*");
    registrationBean.setOrder(2); // set precedence

    return registrationBean;
  }

  @Bean
  @ConditionalOnProperty(prefix = "langer.security.crm-client", value = "enabled")
  public FilterRegistrationBean securityFilter()
  {
    log.info("Generating security filter registration bean");
    FilterRegistrationBean<SecurityFilter> registrationBean = new FilterRegistrationBean<>();
    SecurityFilter secFilter = new SecurityFilter(secret);

    registrationBean.setFilter(secFilter);
    registrationBean.addUrlPatterns("/api2/crm/*");
    registrationBean.setOrder(1); // set precedence

    return registrationBean;
  }


}
