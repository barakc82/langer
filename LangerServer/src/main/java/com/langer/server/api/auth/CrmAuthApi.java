package com.langer.server.api.auth;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.langer.server.api.auth.dto.LoginRequest;
import com.langer.server.api.auth.dto.LoginResponse;
import com.langer.server.api.auth.dto.RegisterRequest;
import com.langer.server.api.auth.dto.RegisterResponse;

import javax.validation.Valid;

@RequestMapping(path = "/auth")
public interface CrmAuthApi
{
    String CRM_API_PATH_REGISTER =                    "register";
    String CRM_API_PATH_LOGIN    =                    "login";

    @RequestMapping(method = RequestMethod.POST, path = CRM_API_PATH_REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest loginRequest);

    @RequestMapping(method = RequestMethod.POST, path = CRM_API_PATH_LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest);
}