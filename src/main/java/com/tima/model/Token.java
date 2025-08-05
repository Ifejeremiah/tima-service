package com.tima.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Token extends BaseObject {
    private String email;
    private String refreshToken;
    private Date expiresAt;
}
