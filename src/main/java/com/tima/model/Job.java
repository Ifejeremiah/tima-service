package com.tima.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.JobStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Job extends BaseObject {
    private String requestId;
    private Integer recordCount;
    @NotBlank(message = "Subject is required")
    @Length(min = 3, max = 100)
    private String subject;
    @NotBlank(message = "Topic is required")
    @Length(min = 3, max = 100)
    private String topic;
    @NotBlank(message = "Difficulty level is required")
    @Pattern(regexp = "^(EASY|MEDIUM|HARD)$", message = "Only EASY, MEDIUM or HARD values are allowed")
    private String difficultyLevel;
    @NotBlank(message = "Mode is required")
    @Pattern(regexp = "^(PRACTICE|EXAM)$", message = "Only PRACTICE or EXAM values are allowed")
    private String mode;
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(ACTIVE|DRAFT)$", message = "Only ACTIVE or DRAFT values are allowed")
    private String status;
    @NotNull(message = "Exam type is required")
    @Length(max = 20)
    private String examType;
    @NotNull(message = "Exam year is required")
    @Length(max = 5)
    private String examYear;
    private JobStatus jobStatus;
    private String originalFileName;
    private String statusMessage;
    @JsonIgnore
    private String filePath;
}
