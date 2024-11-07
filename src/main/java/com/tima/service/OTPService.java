package com.tima.service;

import com.tima.exception.BadRequestException;
import com.tima.exception.NotFoundException;
import com.tima.model.OTP;
import com.tima.repository.OTPRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class OTPService {

    OTPRepository otpRepository;

    OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public String create(String email) {
        try {
            String generatedOTP = generateRandomNumberString();

            OTP otp = new OTP();
            otp.setEmail(email);
            otp.setOtp(generatedOTP);
            otp.setExpiresAt(new Date(System.currentTimeMillis() + 300000));

            otpRepository.save(otp);
            return generatedOTP;
        } catch (Exception error) {
            log.error("Error creating otp", error);
            throw error;
        }
    }

    public String generateRandomNumberString() {
        try {
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            return String.format("%06d", number);
        } catch (Exception error) {
            log.error("Error generating random number string", error);
            throw error;
        }
    }

    public OTP findByEmailAndOtp(String email, String otp) {
        try {
            return otpRepository.findByEmailAndOtp(email, otp).orElseThrow(() -> new NotFoundException("Could not find OTP"));
        } catch (Exception error) {
            log.error("Error fetching OTP by email and otp", error);
            throw error;
        }
    }

    public void checkOTPExpiry(OTP otp) {
        try {
            boolean isExpired = otp.getExpiresAt().before(new Date());
            if (isExpired) throw new BadRequestException("This OTP is expired");
        } catch (Exception error) {
            log.error("Error checking OTP expiry", error);
            throw error;
        }
    }

    public void delete(OTP otp) {
        try {
            otpRepository.deleteById(otp.getId());
        } catch (Exception error) {
            log.error("Error deleting OTP", error);
            throw error;
        }
    }
}
