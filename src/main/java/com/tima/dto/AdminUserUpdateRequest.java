package com.tima.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdminUserUpdateRequest {
    @NotBlank(message = "First name is required")
    @Length(max = 70)
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Length(max = 70)
    private String lastName;
    @NotBlank(message = "Job title is required")
    @Length(max = 50)
    private String jobTitle;
    @NotBlank(message = "Mobile phone is required")
    @Length(max = 20)
    private String mobilePhone;
    @NotBlank(message = "Address is required")
    @Length(max = 50)
    private String address;
    @NotBlank(message = "City is required")
    @Length(max = 20)
    private String city;
    @NotBlank(message = "State is required")
    @Length(max = 20)
    private String state;
    @NotBlank(message = "Country is required")
    @Length(max = 20)
    private String country;
}
