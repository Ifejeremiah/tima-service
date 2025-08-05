package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "refreshToken is required")
    private String refreshToken;
}
