package com.langer.server.api.auth;

import com.langer.server.model.entity.impl.User;
import com.langer.server.model.repository.UserRepository;
import com.langer.server.security.jwt.JwtUtils;
import com.langer.server.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import com.langer.server.api.auth.dto.LoginRequest;
import com.langer.server.api.auth.dto.LoginResponse;
import com.langer.server.api.auth.dto.RegisterRequest;
import com.langer.server.api.auth.dto.RegisterResponse;
import com.langer.server.api.data.enums.UserRole;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CrmAuthController implements CrmAuthApi
{
    private final AuthenticationManager authenticationManager;
    private final JwtUtils              jwtUtils;

    private final UserRepository     userRepository;


    @Override
    public ResponseEntity<RegisterResponse> registerUser(RegisterRequest registerRequest)
    {
        if (userRepository.existsByUsername(registerRequest.getUsername()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setHash(registerRequest.getPassword().hashCode());
        user.setRole(UserRole.Unauthorized);
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponse("User registered successfully! Waiting for an approval from an administrator."));
    }

    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new LoginResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername()));
    }
}