package com.tima.web;

import com.tima.model.*;
import com.tima.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<UserCreateResponse> register(@Validated @RequestBody User user) {
        Response<UserCreateResponse> response = new Response<>();
        response.setData(authService.register(user));
        response.setResponseMessage("User registered and OTP sent via mail successfully");
        return response;
    }

    @PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserLoginResponse> authenticate(@Validated @RequestBody UserLoginRequest userLoginRequest) {
        Response<UserLoginResponse> response = new Response<>();
        response.setData(authService.authenticate(userLoginRequest));
        response.setResponseMessage("User authenticated successfully");
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
        response.setResponseMessage("OTP validated successfully");
        return response;
    }
}
