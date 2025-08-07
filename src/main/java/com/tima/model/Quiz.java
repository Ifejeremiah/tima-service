package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.QuestionDifficultyLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Quiz extends BaseObject {
    private int studentId;
    @NotBlank(message = "Subject is required")
    @Length(min = 3, max = 100)
    private String subject;
    @NotBlank(message = "Topic is required")
    @Length(min = 3, max = 100)
    private String topic;
    private QuestionDifficultyLevel difficultyLevel;
    private Integer score;
    private Integer numberOfQuestions;
}
