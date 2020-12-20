//package com.swingit.crm.client.config.interceptors;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//import swingit.server.crm.CrmApiGlobal;
//
//@Component
//@ConditionalOnProperty(prefix = "feign.compression.response", value = "enabled", havingValue = "true")
//public class UserAgentInterceptor implements RequestInterceptor
//{
//    @Override
//    public void apply(RequestTemplate requestTemplate)
//    {
//        requestTemplate.header("User-Agent", "gzip");
//        requestTemplate.header("Accept-Encoding", "gzip");
//    }
//}