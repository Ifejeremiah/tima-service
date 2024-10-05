package com.tima.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordResetRequest {
    @NotBlank(message = "Email is required")
    @Length(min = 8, max = 40)
    private String email;
}
