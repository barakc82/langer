package com.langer.server.api.swagger;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!swagger")
public class SwaggerOverrideController {

    @Autowired
    Environment env;

    @GetMapping("/swagger-ui.html")
    String nope(){
        return "swagger is disabled, loaded profiles are: " + Lists.asList("", env.getActiveProfiles());
    }
}
