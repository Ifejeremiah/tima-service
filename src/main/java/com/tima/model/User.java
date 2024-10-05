package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Document(collection = "user_login")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User extends BaseObject {
    @NotBlank(message = "First name is required")
    @Length(min = 3, max = 30)
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Length(min = 3, max = 30)
    private String lastName;
    @Email
    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Length(min = 8, max = 40)
    private String email;
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 20)
    private String password;
    private Status status = Status.INACTIVE;
    private Boolean emailConfirmed = false;
}
