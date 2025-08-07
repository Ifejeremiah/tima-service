package com.tima.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
public class StartQuizRequest {
    @NotBlank(message = "Subject is required")
    @Length(min = 3, max = 100)
    private String subject;
    @NotBlank(message = "Topic is required")
    @Length(min = 3, max = 100)
    private String topic;
    @NotBlank(message = "Difficulty level is required")
    @Pattern(regexp = "^(EASY|MEDIUM|HARD)$", message = "Only EASY, MEDIUM or HARD values are allowed")
    private String difficultyLevel;
    @Min(10)
    @Max(500)
    @NotNull(message = "Number of questions is required")
    private Integer numberOfQuestions;
}
