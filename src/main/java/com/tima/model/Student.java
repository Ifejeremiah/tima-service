package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Student extends BaseObject {
    private int userId;
    @NotBlank(message = "Name is required")
    @Length(min = 3, max = 70)
    private String name;
    @NotBlank(message = "Age range is required")
    @Length(min = 1, max = 10)
    private String ageRange;
    @Email
    @NotBlank(message = "Guardian's email is required")
    @Length(min = 8, max = 70)
    private String guardianEmail;
    private Boolean agreeToTerms;
    @NotBlank(message = "Name of school is required")
    @Length(min = 3, max = 100)
    private String nameOfSchool;
    @NotBlank(message = "Education level is required")
    @Length(min = 3, max = 60)
    private String educationLevel;
    @NotBlank(message = "Grade is required")
    @Length(min = 3, max = 20)
    private String grade;
    private Set<String> examTypes;
    @Length(min = 3, max = 225)
    private String goalDescription;
    private Set<String> learningGoals;
    private String learningTime;
    private Boolean canNotify;
}
