package com.tima.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Otp extends BaseObject {
    private String email;
    private String otp;
    private Date expiresAt;
}
