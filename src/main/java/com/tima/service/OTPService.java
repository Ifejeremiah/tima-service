package com.tima.service;

import com.tima.dao.OtpDao;
import com.tima.exception.BadRequestException;
import com.tima.exception.NotFoundException;
import com.tima.model.Otp;
import com.tima.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class OTPService {

    OtpDao otpDao;

    OTPService(OtpDao otpDao) {
        this.otpDao = otpDao;
    }

    public String create(String email) {
        try {
            String generatedOTP = generateRandomNumberString();

            Otp otp = new Otp();
            otp.setEmail(email);
            otp.setOtp(generatedOTP);
            otp.setExpiresAt(new DateUtil(new Date(System.currentTimeMillis() + 300000)).getDateInGMT());

            otpDao.create(otp);
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

    public Otp findByEmailAndOtp(String email, String otp) {
        try {
            Otp existing = otpDao.findByEmailAndOTP(email, otp);
            if (existing == null) throw new NotFoundException("Could not find OTP");
            return existing;
        } catch (Exception error) {
            log.error("Error fetching OTP by email and otp", error);
            throw error;
        }
    }

    public void checkOTPExpiry(Otp otp) {
        try {
            Date currentDateTime = new DateUtil().parseDate(new DateUtil(new Date()).getDateInGMT());
            boolean isExpired = new DateUtil().parseDate(otp.getExpiresAt()).before(currentDateTime);

            if (isExpired) throw new BadRequestException("This OTP is expired");
        } catch (Exception error) {
            log.error("Error checking OTP expiry", error);
            throw error;
        }
    }

    public void delete(Otp otp) {
        try {
            otpDao.delete(otp.getId());
        } catch (Exception error) {
            log.error("Error deleting OTP", error);
            throw error;
        }
    }
}
