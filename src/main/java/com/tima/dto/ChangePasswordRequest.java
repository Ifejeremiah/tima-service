package com.tima.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordRequest {
    @Length(min = 8, max = 20)
    @NotBlank(message = "New password is required")
    private String newPassword;
}
