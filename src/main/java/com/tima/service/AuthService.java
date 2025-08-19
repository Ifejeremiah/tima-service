package com.tima.service;

import com.tima.dto.*;
import com.tima.enums.UserStatus;
import com.tima.exception.BadRequestException;
import com.tima.exception.DuplicateEntityException;
import com.tima.model.Otp;
import com.tima.model.Token;
import com.tima.model.User;
import com.tima.util.Encoder;
import com.tima.util.JwtUtil;
import com.tima.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class AuthService {
    UserService userService;
    MailService mailService;
    OTPService otpService;
    Encoder encoder;
    JwtUtil jwtUtil;
    TokenService tokenService;

    public AuthService(UserService userService, MailService mailService, OTPService otpService, Encoder encoder, JwtUtil jwtUtil, TokenService tokenService) {
        this.userService = userService;
        this.mailService = mailService;
        this.otpService = otpService;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    public UserCreateResponse register(User user) {
        try {
            checkEmailExists(user.getEmail());
            user.setPassword(encoder.encodePassword(user.getPassword()));
            String otp = otpService.create(user.getEmail());
            mailService.sendMail(user.getEmail(), MailUtil.constructOTPMail(otp));
            return buildCreateResponse(user);
        } catch (Exception error) {
            log.error("Error registering user", error);
            throw error;
        }
    }

    private void checkEmailExists(String email) {
        if (userService.findByEmail(email) != null)
            throw new DuplicateEntityException("User with this email already exists");
    }

    private UserCreateResponse buildCreateResponse(User user) {
        long id = userService.create(user);
        return new UserCreateResponse(id, user.getEmail());
    }

    public UserLoginResponse authenticate(UserLoginRequest loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            checkUserExists(user);
            checkUserIsNotDeleted(user);
            validatePassword(loginRequest.getPassword(), user.getPassword());
            userService.updateLastLogin(user.getId());
            return buildLoginResponse(user);
        } catch (Exception error) {
            log.error("Error authenticating user", error);
            throw error;
        }
    }

    private void checkUserExists(User user) {
        if (user == null)
            throw new BadRequestException("Invalid credentials");
    }

    private UserLoginResponse buildLoginResponse(User user) {
        UserLoginResponse response = new UserLoginResponse(user);
        response.setAccessToken(jwtUtil.generateAccessToken(user.getEmail()));
        String refreshToken = jwtUtil.generateRefreshToken();
        tokenService.create(user.getEmail(), refreshToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    private UserLoginResponse buildLoginResponse(User user, String refreshToken) {
        UserLoginResponse response = new UserLoginResponse(user);
        response.setAccessToken(jwtUtil.generateAccessToken(user.getEmail()));
        response.setRefreshToken(refreshToken);
        return response;
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!encoder.validatePassword(rawPassword, encodedPassword))
            throw new BadRequestException("Invalid credentials");
    }

    private void checkUserIsNotDeleted(User user) {
        if (UserStatus.DELETED.equals(user.getStatus()))
            throw new BadRequestException("Account blocked - contact administrator");
    }

    public void resetPassword(PasswordResetRequest resetRequest) {
        try {
            User user = userService.findByEmail(resetRequest.getEmail(), true);
            String otp = otpService.create(user.getEmail());
            mailService.sendMail(user.getEmail(), MailUtil.constructOTPMail(otp));
        } catch (Exception error) {
            log.error("Error resetting password", error);
            throw error;
        }
    }

    public UserLoginResponse validateOTP(OTPRequest otpRequest) {
        try {
            User user = userService.findByEmail(otpRequest.getEmail(), true);
            Otp otp = otpService.findByEmailAndOtp(user.getEmail(), otpRequest.getOtp());
            otpService.checkOTPExpiry(otp);
            otpService.delete(otp);
            userService.activateEmail(user.getEmail());
            return buildLoginResponse(user);
        } catch (Exception error) {
            log.error("Error validating OTP", error);
            throw error;
        }
    }

    public UserLoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            User user = userService.findByEmail(refreshTokenRequest.getEmail());
            checkIfUserExists(user);
            checkUserIsNotDeleted(user);
            Token token = tokenService.findByEmailAndToken(user.getEmail(), refreshTokenRequest.getRefreshToken());
            checkIfTokenIsExpired(token);
            userService.updateLastLogin(user.getId());
            return buildLoginResponse(user, refreshTokenRequest.getRefreshToken());
        } catch (Exception error) {
            log.error("Error refreshing token", error);
            throw error;
        }
    }

    private void checkIfUserExists(User user) {
        if (user == null)
            throw new BadRequestException("Email does not exist");
    }

    private void checkIfTokenIsExpired(Token token) {
        if (token.getExpiresAt().before(new Date())) {
            throw new BadRequestException("Refresh token is expired. Login to regenerate");
        }
    }
}
