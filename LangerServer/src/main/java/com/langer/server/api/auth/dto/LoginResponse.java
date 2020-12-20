package com.langer.server.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse
{
        private String token;
        @Builder.Default
        private String type = "Bearer";
        private Long id;
        private String username;

        public LoginResponse(String accessToken, Long id, String username)
        {
            this.token = accessToken;
            this.id = id;
            this.username = username;
        }
}