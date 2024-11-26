package com.tima.web;

import com.tima.dto.UserCreateResponse;
import com.tima.dto.UserLoginRequest;
import com.tima.dto.UserLoginResponse;
import com.tima.model.OTPRequest;
import com.tima.model.PasswordResetRequest;
import com.tima.model.Response;
import com.tima.model.User;
import com.tima.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<UserCreateResponse> register(@Validated @RequestBody User user) {
        Response<UserCreateResponse> response = new Response<>();
        response.setData(authService.register(user));
        response.setMessage("User registered and OTP sent via mail successfully");
        return response;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserLoginResponse> authenticate(@Validated @RequestBody UserLoginRequest userLoginRequest) {
        Response<UserLoginResponse> response = new Response<>();
        response.setData(authService.authenticate(userLoginRequest));
        response.setMessage("User authenticated successfully");
        return response;
    }

    @PostMapping(path = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> resetPassword(@Validated @RequestBody PasswordResetRequest resetRequest) {
        authService.resetPassword(resetRequest);
        return new Response<>("OTP sent via mail successfully");
    }

    @PostMapping(path = "/validate-otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserLoginResponse> validateOTP(@Validated @RequestBody OTPRequest otpRequest) {
        Response<UserLoginResponse> response = new Response<>();
        response.setData(authService.validateOTP(otpRequest));
        response.setMessage("OTP validated successfully");
        return response;
    }
}
