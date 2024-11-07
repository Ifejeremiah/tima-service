package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@Document(collection = "students")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Student {
    @NotBlank(message = "Name is required")
    @Length(min = 3, max = 40)
    private String name;
    @NotBlank(message = "Age range is required")
    @Length(min = 1, max = 10)
    private String ageRange;
    @NotBlank(message = "Guardian's email is required")
    @Length(min = 1, max = 10)
    private String guardianEmail;
    private Boolean agreeToTerms;
    @NotBlank(message = "School name is required")
    @Length(min = 3, max = 40)
    private String schoolName;
    @NotBlank(message = "Educational level is required")
    @Length(min = 3, max = 20)
    private String educationLevel;
    private Set<String> examTypes;
    @Length(min = 3, max = 155)
    private String goalDescription;
    private Set<String> learningGoals;
    private String learningTime;
    private Boolean canNotify;
}
