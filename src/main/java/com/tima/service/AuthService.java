package com.tima.service;

import com.tima.exception.BadRequestException;
import com.tima.exception.DuplicateEntityException;
import com.tima.model.*;
import com.tima.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    UserService userService;
    MailService mailService;
    OTPService otpService;
    BCryptPasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserService userService, MailService mailService, OTPService otpService, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.mailService = mailService;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserCreateResponse register(User user) {
        try {
            checkEmailExists(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return buildCreateResponse(user);
        } catch (Exception error) {
            log.error("Error registering user", error);
            throw error;
        }
    }

    private void checkEmailExists(User user) {
        if (userService.existsByEmail(user.getEmail()))
            throw new DuplicateEntityException("User with this email already exists");
    }

    private UserCreateResponse buildCreateResponse(User user) {
        return new UserCreateResponse(userService.create(user));
    }

    public UserLoginResponse authenticate(UserLoginRequest loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            validatePassword(loginRequest.getPassword(), user.getPassword());
            return buildLoginResponse(user);
        } catch (Exception error) {
            log.error("Error authenticating user", error);
            throw error;
        }
    }

    private UserLoginResponse buildLoginResponse(User user) {
        UserLoginResponse response = new UserLoginResponse(user);
        response.setAccessToken(jwtUtil.generateToken(user.getEmail()));
        return response;
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw new BadRequestException("Invalid credentials");
    }

    public void resetPassword(PasswordResetRequest resetRequest) {
        try {
            User user = userService.findByEmail(resetRequest.getEmail());
            String otp = otpService.create(user.getEmail());
            mailService.sendMail(user.getEmail(), otp);
        } catch (Exception error) {
            log.error("Error resetting password", error);
            throw error;
        }
    }

    public UserLoginResponse validateOTP(OTPRequest otpRequest) {
        try {
            User user = userService.findByEmail(otpRequest.getEmail());
            OTP otp = otpService.findByEmailAndOtp(user.getEmail(), otpRequest.getOtp());
            otpService.checkOTPExpiry(otp);
            otpService.delete(otp);
            return buildLoginResponse(user);
        } catch (Exception error) {
            log.error("Error validating OTP", error);
            throw error;
        }
    }
}
