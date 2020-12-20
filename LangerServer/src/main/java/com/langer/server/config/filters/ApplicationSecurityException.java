package com.langer.server.config.filters;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class ApplicationSecurityException extends SecurityException
{
    public ApplicationSecurityException(String unauthorized)
    {
        super(unauthorized);
    }
}