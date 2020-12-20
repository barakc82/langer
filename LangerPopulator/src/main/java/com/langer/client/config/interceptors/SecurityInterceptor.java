package com.langer.client.config.interceptors;

import com.langer.server.api.LangerApiGlobal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityInterceptor implements RequestInterceptor
{
    @Value("${langer.security.client.secret}")
    private String secret;

    @Override
    public void apply(RequestTemplate requestTemplate)
    {
        requestTemplate.header(LangerApiGlobal.HEADER_AUTH, secret);
    }
}