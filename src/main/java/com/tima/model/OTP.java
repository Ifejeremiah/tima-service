package com.tima.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "otps")
public class OTP extends BaseObject {
    private String email;
    private String otp;
    private Date expiresAt;
}
