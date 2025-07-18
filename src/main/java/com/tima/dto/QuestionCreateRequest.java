package com.tima.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashSet;

@Getter
@Setter
public class QuestionCreateRequest {
    @NotBlank(message = "Question is required")
    @Length(min = 10)
    private String question;
    private LinkedHashSet<String> options;
    @NotBlank(message = "Answer is required")
    @Length(min = 3, max = 225)
    private String answer;
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
    @NotBlank(message = "Exam type is required")
    @Pattern(regexp = "^(WAEC|JAMB)$", message = "Only WAEC or JAMB values are allowed")
    private String examType;
}
