package com.langer.client.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FeignConfiguration
{
    private final ObjectMapper mapper;

    @Bean
    public Encoder feignEncoder()
    {
        return new JacksonEncoder(mapper);
    }

    @Bean
    public Decoder feignDecoder()
    {
        return new JacksonDecoder(mapper);
    }
}