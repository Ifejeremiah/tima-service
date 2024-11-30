package com.tima.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User extends BaseObject {
    @Email
    @NotBlank(message = "Email is required")
    @Length(min = 8, max = 40)
    private String email;
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 20)
    @JsonIgnore
    private String password;
    private UserStatus status;
    private Boolean emailConfirmed;
    private LocalDateTime lastLoginOn;
}
