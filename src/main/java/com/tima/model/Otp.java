package com.tima.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Otp extends BaseObject {
    private String email;
    private String otp;
    private String expiresAt;
}
