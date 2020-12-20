//package com.swingit.crm.client.config.interceptors;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.stereotype.Component;
//
////@Component
//public class GzipResponseInterceptor implements RequestInterceptor // TODO yshabi add a RESponsE intercept somehow
//{
//    @Override
//    public void apply(RequestTemplate requestTemplate)
//    {
//        requestTemplate.header("User-Agent", "gzip");
//        requestTemplate.header("Accept-Encoding", "gzip");
//    }
//}