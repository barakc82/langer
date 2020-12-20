package com.langer.crm.server.api.router;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RouterController implements ErrorController
{
    @RequestMapping("/error")
    public String handleError() {
        return "/";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}